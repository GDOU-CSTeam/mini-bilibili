package com.bili.web.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bili.pojo.constant.RedisConstants;
import com.bili.pojo.constant.SystemConstants;
import com.bili.common.utils.*;
import com.bili.pojo.dto.EditUserInfoDTO;
import com.bili.pojo.dto.PhoneLoginDTO;
import com.bili.pojo.dto.ResetPwdByPhoneDTO;
import com.bili.pojo.entity.LoginUser;
import com.bili.pojo.entity.User;
import com.bili.pojo.entity.UserInfo;
import com.bili.web.mapper.MenuMapper;
import com.bili.web.mapper.UserMapper;
import com.bili.web.service.IUserInfoService;
import com.bili.web.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private MailCodeUtil mailCodeUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private IUserInfoService userInfoService;
    @Resource
    JwtUtil jwtUtil;

    /**
     * 账号密码登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public Result nameLogin(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = jwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:" + userId, loginUser, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES );
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return Result.success(map, "登录成功");
    }

    /**
     * 用户登出
     * @return
     */
    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+ userId);
        return Result.success("退出成功");
    }

    /**
     * 短信登录及注册
     * @param loginDTO
     * @return
     */
    @Override
    public Result phoneLogin(PhoneLoginDTO loginDTO) {
        // 1.校验手机号
        String phoneNumber = loginDTO.getPhoneNumber();
        if (RegexUtils.isPhoneInvalid(phoneNumber)) {
            // 2.如果不符合，返回错误信息
            return Result.failed("手机号格式错误！");
        }
        // 3.从redis获取验证码并校验
        String cacheCode = redisCache.getCacheObject(RedisConstants.LOGIN_CODE_KEY + phoneNumber);
        String code = loginDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致，报错
            return Result.failed("验证码错误");
        }

        // 4.一致，根据手机号查询用户 select * from tb_user where phone = ?
        User user = query().eq("phone_number", phoneNumber).one();

        // 5.判断用户是否存在
        if (user == null) {
            // 6.不存在，创建新用户并保存
            user = createUserWithPhone(phoneNumber);

        }

        //根据用户查询权限信息，添加到LoginUser中
        List<String> list = menuMapper.selectPermsByUserId(user.getId());

        //根据用户查询权限信息，添加到LoginUser中
        LoginUser loginUser = new LoginUser(user, list);
        //生成jwt令牌
        String jwt = jwtUtil.createJWT(user.getId().toString());
        //authenticate存入redis
        redisCache.setCacheObject("login:" + user.getId(), loginUser, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES );
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return Result.success(map,"登录成功");
    }

    /**
     * 邮箱登录及注册
     * @param email
     * @param code
     * @return
     */
    @Override
    public Result emailLogin(String email, String code) {
        // 1.校验邮箱
        if (RegexUtils.isEmailInvalid(email)) {
            // 2.如果不符合，返回错误信息
            return Result.failed("邮箱格式错误！");
        }
        // 3.从redis获取验证码并校验
        String cacheCode = redisCache.getCacheObject(RedisConstants.LOGIN_CODE_KEY + email);
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致，报错
            return Result.failed("验证码错误");
        }

        // 4.一致，根据手机号查询用户 select * from tb_user where phone = ?
        User user = query().eq("email", email).one();

        // 5.判断用户是否存在
        if (user == null) {
            // 6.不存在，创建新用户并保存
            user = createUserWithEmail(email);

        }

        //根据用户查询权限信息，添加到LoginUser中
        List<String> list = menuMapper.selectPermsByUserId(user.getId());

        //根据用户查询权限信息，添加到LoginUser中
        LoginUser loginUser = new LoginUser(user, list);
        //生成jwt令牌
        String jwt = jwtUtil.createJWT(user.getId().toString());
        //authenticate存入redis
        redisCache.setCacheObject("login:" + user.getId(), loginUser, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES );
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return Result.success(map,"登录成功");
    }



    /**
     * 用手机短信重置密码
     * @param resetPwdDTO
     * @return
     */
    @Override
    public Result resetPwdByPhone(ResetPwdByPhoneDTO resetPwdDTO) {
        // 1.校验手机号
        String phone = resetPwdDTO.getPhone();
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 2.如果不符合，返回错误信息
            return Result.failed("手机号格式错误！");
        }
        // 3.从redis获取验证码并校验
        String cacheCode = redisCache.getCacheObject(RedisConstants.LOGIN_CODE_KEY + phone);
        String code = resetPwdDTO.getCode();
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致，报错
            return Result.failed("验证码错误");
        }

        // 4.一致，根据手机号查询用户 select * from tb_user where phone = ?
        User user = query().eq("phone_number", phone).one();

        // 5.判断用户是否存在
        if (user == null) {
            // 6.不存在，返回用户不存在
            return Result.failed("用户不存在");
        }

        //验证两次密码是否一致
        if(!resetPwdDTO.getNewPwd().equals(resetPwdDTO.getConfirmPwd())){
            //不一致，返回错误
            return Result.failed("两次密码不一致，请修改");
        }
        //密码加密储存
        String resetPwd = passwordEncoder.encode(resetPwdDTO.getNewPwd());
        //开始修改密码
        update().set("password", resetPwd).eq("id", user.getId()).update();
        return Result.success("修改密码成功");
    }

    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    @Override
    public Result sendEmailCode(String email) {
        // 1.校验邮箱
        if (RegexUtils.isEmailInvalid(email)) {
            // 2.如果不符合，返回错误信息
            return Result.failed("邮箱格式错误！");
        }
        // 3.符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        //4.保存验证码到redis  set key value ex 120
        redisCache.setCacheObject(RedisConstants.LOGIN_CODE_KEY + email, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 5.发送验证码
        Result result;
        try {
            result = mailCodeUtil.sendCode(email);
        } catch (Exception e) {
            return Result.failed("验证码发送错误！");
        }
        // 返回结果
        return result;
    }


    /**
     * 邮箱验证码重置密码
     * @param newPwd
     * @param confirmPwd
     * @return
     */
    @Override
    public Result resetPwdByEmail(String newPwd, String confirmPwd) {
        //1.获取当前用户
        Long userId = UserDTOHolder.getUserDTO().getId();
        User user = query().eq("id", userId).one();
        // 2. 检查Redis中是否有已验证的标志
        String verifiedStatus = redisCache.getCacheObject(RedisConstants.VERIFIED_CODE_KEY + user.getEmail());
        if (!"verified".equals(verifiedStatus)) {
            return Result.failed("请先验证验证码");
        }

        //3. 验证两次密码是否一致
        if(!newPwd.equals(confirmPwd)){
            //不一致，返回错误
            return Result.failed("两次密码不一致，请修改");
        }
        //4. 密码加密储存
        String resetPwd = passwordEncoder.encode(newPwd);
        //5. 开始修改密码
        update().set("password", resetPwd).eq("id", user.getId()).update();
        //6.移除redis中的验证标志
        redisCache.deleteObject(RedisConstants.VERIFIED_CODE_KEY + user.getEmail());
        return Result.success("修改密码成功");
    }


    /**
     * 发送短信验证码
     * @param phoneNumber
     * @return
     */
    @Override
    public Result sendPhoneCode(String phoneNumber) {
        // 1.校验手机号
        if (RegexUtils.isPhoneInvalid(phoneNumber)) {
            // 2.如果不符合，返回错误信息
            return Result.failed("手机号格式错误！");
        }
        // 3.符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        //4.保存验证码到redis  set key value ex 120
        redisCache.setCacheObject(RedisConstants.LOGIN_CODE_KEY + phoneNumber, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 5.TODO 发送验证码
        log.debug("发送短信验证码成功，验证码：{}", code);
        // 返回ok
        return Result.success("发送短信验证码成功");
    }


    /**
     * 用手机号创建新用户
     * @param phoneNumber
     * @return
     */
    private User createUserWithPhone(String phoneNumber) {
        // 加密密码
        String BCryptPassword = passwordEncoder.encode(RandomUtil.randomString(4));
        //生成账号
        String name = SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(6);
        //1.创建用户
        User user = User.builder()
                .userName(name)
                .nickName(name)
                .password(BCryptPassword)
                .phoneNumber(phoneNumber)
                .status("0") //状态正常
                .userType("1") //普通用户
                .build();
        //2.保存用户到数据库
        save(user);
        return getById(user.getId());
    }

    /**
     * 用邮箱创建新用户
     * @param email
     * @return
     */
    private User createUserWithEmail(String email) {
        // 加密密码
        String BCryptPassword = passwordEncoder.encode(RandomUtil.randomString(4));
        //生成账号
        String name = SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(6);
        //1.创建用户
        User user = User.builder()
                .userName(name)
                .nickName(name)
                .password(BCryptPassword)
                .email(email)
                .status("0") //状态正常
                .userType("1") //普通用户
                .build();
        //2.保存用户到数据库
        save(user);
        return getById(user.getId());
    }

    /**
     * 签到
     * @return
     */
    @Override
    public Result sign() {
        // 1.获取当前登录用户
        Long userId = UserDTOHolder.getUserDTO().getId();
        // 2.获取日期
        LocalDateTime now = LocalDateTime.now();
        // 3.拼接key
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = RedisConstants.USER_SIGN_KEY + userId + keySuffix;
        // 4.获取今天是本月的第几天
        int dayOfMonth = now.getDayOfMonth();
        // 新增检查是否已经签到
        boolean isSigned = stringRedisTemplate.opsForValue().getBit(key, dayOfMonth - 1);
        if (isSigned) {
            return Result.failed("您今天已经签到过了！");
        }
        // 5.写入Redis SETBIT key offset 1
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        //6. 每天登录可以获得1个硬币和5点经验
        userInfoService.update()
                .eq("user_id", userId)
                .setSql("coins = coins + 1")
                .setSql("experience = experience + 5")
                .update();
        return Result.success();
    }

    /**
     * 修改用户简单信息
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public Result updateUserInfo(EditUserInfoDTO dto) {
        // 1.获取当前登录用户
        Long userId = UserDTOHolder.getUserDTO().getId();
        //2. 更新User表
        User user = new User();
        user.setId(userId);
        user.setUpdateTime(LocalDateTime.now());
        if (dto.getNickName() != null && dto.getNickName() != "") {
            //修改昵称消耗6个硬币，并且不能重复
            long count = count(new QueryWrapper<User>().eq("nick_name", dto.getNickName()));
            if(count > 0){
                return Result.failed("昵称已存在");
            }
            user.setNickName(dto.getNickName());

            //2.3 判断用户硬币是否足够
            Integer currentCoins = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", userId)).getCoins();
            // 检查是否有足够的 coins 进行扣减
            if (currentCoins < 6) {
                // 返回错误信息
                return Result.failed("您的硬币不足！");
            } else {
                // 执行扣减操作，扣除六个硬币
                userInfoService.update()
                        .eq("user_id", userId)
                        .setSql("coins = coins - 6")
                        .update();
            }
        }
        if (dto.getAvatar() != null && dto.getAvatar() != "") {
            user.setAvatar(dto.getAvatar());
        }
        updateById(user);
        //3.更新UserInfo表
        UserInfo userInfo = new UserInfo();
        if (dto.getIntro() != null && dto.getIntro() != "") {
            userInfo.setIntro(dto.getIntro());
        }
        if (dto.getBirthday() != null) {
            userInfo.setBirthday(dto.getBirthday());
        }
        if (dto.getBjCover() != null && dto.getBjCover() != "") {
            userInfo.setBjCover(dto.getBjCover());
        }
        userInfoService.update(userInfo, new QueryWrapper<UserInfo>().eq("user_id",userId));
        //4.返回成功结果
        return Result.success();
    }

    /**
     * 更换新邮箱，绑定邮箱
     * @param newEmail
     * @return
     */
    @Override
    @Transactional
    public Result updateUserEmail(String newEmail) {
        //1.检查该邮箱是否被占用
        long count = count(new QueryWrapper<User>().eq("email", newEmail));
        if(count > 0){
            return Result.failed("该邮箱已经被占用");
        }
        //2.获取当前用户
        Long userId = UserDTOHolder.getUserDTO().getId();
        User user = query().eq("id", userId).one();
        //3.检查Redis中是否有已验证的标志
        String verifiedOne = redisCache.getCacheObject(RedisConstants.VERIFIED_CODE_KEY + user.getEmail());
        String verifiedTwo = redisCache.getCacheObject(RedisConstants.VERIFIED_CODE_KEY + newEmail);
        if (!"verified".equals(verifiedOne) || !"verified".equals(verifiedTwo)) {
            return Result.failed("请先验证验证码");
        }
        //3. 更新用户邮箱
        update().eq("id", userId).set("email", newEmail).update();
        //4. 移除redis中的验证标志
        redisCache.deleteObject(RedisConstants.VERIFIED_CODE_KEY + user.getEmail());
        return Result.success();
    }

    /**
     * 更换新手机号，绑定手机号
     * @param newPhone
     * @return
     */
    @Override
    public Result updateUserPhone(String newPhone) {
        //1.检查该手机号是否被占用
        long count = count(new QueryWrapper<User>().eq("phone_number", newPhone));
        if(count > 0){
            return Result.failed("该手机号已经被占用");
        }
        //2.获取当前用户
        Long userId = UserDTOHolder.getUserDTO().getId();
        User user = query().eq("id", userId).one();
        //3.检查Redis中是否有已验证的标志
        String verifiedOne = redisCache.getCacheObject(RedisConstants.VERIFIED_CODE_KEY + user.getPhoneNumber());
        String verifiedTwo = redisCache.getCacheObject(RedisConstants.VERIFIED_CODE_KEY + newPhone);
        if (!"verified".equals(verifiedOne) || !"verified".equals(verifiedTwo)) {
            return Result.failed("请先验证验证码");
        }
        //3. 更新用户手机号
        update().eq("id", userId).set("phone_number", newPhone).update();
        //4. 移除redis中的验证标志
        redisCache.deleteObject(RedisConstants.VERIFIED_CODE_KEY + newPhone);
        return Result.success();
    }

    /**
     * 验证邮箱验证码
     * @param email
     * @param code
     * @return
     */
    @Override
    public Result verifyEmailCode(String email, String code) {
        // 1. 校验邮箱格式
        if (RegexUtils.isEmailInvalid(email)) {
            return Result.failed("邮箱格式错误！");
        }

        // 2. 从Redis获取验证码并校验
        String cacheCode = redisCache.getCacheObject(RedisConstants.LOGIN_CODE_KEY + email);
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 验证码不一致，报错
            return Result.failed("验证码错误");
        }

        // 3. 设置一个标志，表明该用户的验证码已被验证
        redisCache.setCacheObject(RedisConstants.VERIFIED_CODE_KEY + email, "verified", RedisConstants.VERIFIED_CODE_TTL, TimeUnit.MINUTES);
        //4. 移除验证码
        redisCache.deleteObject(RedisConstants.LOGIN_CODE_KEY + email);

        return Result.success("验证码验证成功");
    }


    /**
     * 验证手机验证码
     * @param phone
     * @param code
     * @return
     */
    @Override
    public Result verifyPhoneCode(String phone, String code) {
        // 1. 校验手机号格式
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.failed("手机号格式错误！");
        }

        // 2. 从Redis获取验证码并校验
        String cacheCode = redisCache.getCacheObject(RedisConstants.LOGIN_CODE_KEY + phone);
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 验证码不一致，报错
            return Result.failed("验证码错误");
        }

        // 3. 设置一个标志，表明该用户的验证码已被验证
        redisCache.setCacheObject(RedisConstants.VERIFIED_CODE_KEY + phone, "verified", RedisConstants.VERIFIED_CODE_TTL, TimeUnit.MINUTES);
        //4. 移除验证码
        redisCache.deleteObject(RedisConstants.LOGIN_CODE_KEY + phone);

        return Result.success("验证码验证成功");
    }
}
