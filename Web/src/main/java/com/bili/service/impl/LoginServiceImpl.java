package com.bili.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bili.constant.RedisConstants;
import com.bili.constant.SystemConstants;
import com.bili.dto.NameLoginDTO;
import com.bili.dto.PhoneLoginDTO;
import com.bili.dto.ResetPwdDTO;
import com.bili.dto.UserDTO;
import com.bili.entity.LoginUser;
import com.bili.entity.User;
import com.bili.mapper.MenuMapper;
import com.bili.mapper.UserMapper;
import com.bili.service.LoginService;
import com.bili.utils.JwtUtil;
import com.bili.utils.RedisCache;
import com.bili.utils.RegexUtils;
import com.bili.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginServiceImpl extends ServiceImpl<UserMapper, User> implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public Result login(NameLoginDTO loginDTO) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getUserName(), loginDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        //authenticate存入redis
        redisCache.setCacheObject("login:" + userId, loginUser, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES );
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return new Result(200,"登录成功",map);
    }

    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        redisCache.deleteObject("login:"+ userId);
        return Result.success(200,"退出成功");
    }

    @Override
    public Result login(PhoneLoginDTO loginDTO) {
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
        String jwt = JwtUtil.createJWT(user.getId().toString());
        //authenticate存入redis
        redisCache.setCacheObject("login:" + user.getId(), loginUser, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES );
        //把token响应给前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token",jwt);
        return new Result(200,"登录成功",map);
    }

    /**
     * 重置密码
     * @param resetPwdDTO
     * @return
     */
    @Override
    public Result resetPassword(ResetPwdDTO resetPwdDTO) {
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
        return Result.success(200, "修改密码成功");
    }


    /**
     * 发送短信验证码
     * @param phoneNumber
     * @return
     */
    @Override
    public Result sendCode(String phoneNumber) {
        // 1.校验手机号
        if (RegexUtils.isPhoneInvalid(phoneNumber)) {
            // 2.如果不符合，返回错误信息
            return Result.failed("手机号格式错误！");
        }
        // 3.符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        //4.保存验证码到redis  set key value ex 120
        redisCache.setCacheObject(RedisConstants.LOGIN_CODE_KEY + phoneNumber, code, RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 5.发送验证码
        log.debug("发送短信验证码成功，验证码：{}", code);
        // 返回ok
        return Result.success(200, "发送短信验证码成功");
    }


    /**
     * 创建新用户
     * @param phoneNumber
     * @return
     */
    private User createUserWithPhone(String phoneNumber) {
        // 加密密码
        String BCryptPassword = passwordEncoder.encode(RandomUtil.randomString(4));
        //1.创建用户
        User user = User.builder()
                .userName(SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(10))
                .password(BCryptPassword)
                .phoneNumber(phoneNumber)
                .status("0") //状态正常
                .userType("1") //普通用户
                .build();
        //2.保存用户到数据库
        save(user);
        return getById(user.getId());
    }
}
