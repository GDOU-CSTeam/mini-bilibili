package com.bili.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bili.common.utils.JwtUtil;
import com.bili.common.utils.MailCodeUtil;
import com.bili.common.utils.RedisCache;
import com.bili.pojo.constant.user.WebRedisConstants;
import com.bili.pojo.dto.user.*;
import com.bili.pojo.entity.user.BUser;
import com.bili.pojo.entity.user.BUserInfo;
import com.bili.pojo.mapper.user.BUserInfoMapper;
import com.bili.pojo.mapper.user.BUserMapper;
import com.bili.common.utils.Result;
import com.bili.web.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    BUserMapper bUserMapper;
    @Resource
    BUserInfoMapper bUserInfoMapper;
    @Resource
    RedisCache redisCache;
    @Resource
    JwtUtil jwtUtil;
    @Resource
    MailCodeUtil mailCodeUtil;
    @Resource
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Result signIn(Long userId) {
        if(redisCache.getCacheObject(WebRedisConstants.USER_TODAY_SIGN_IN_KEY + userId) != null){
            return Result.failed("今日已签到");
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
        Duration duration = Duration.between(now, endOfDay);
        redisCache.setCacheObject(WebRedisConstants.USER_TODAY_SIGN_IN_KEY + userId, 1, duration.toSeconds(), TimeUnit.SECONDS);
        return Result.success("签到成功");
    }

    @Override
    public Result getUserInfo(Long userId) {
        BUserInfo bUserInfo = bUserInfoMapper.selectById(userId);
        return Result.success(bUserInfo);
    }

    @Override
    public Result usernameLogin(UserNameLoginParam userNameLoginParam) {
        LambdaQueryWrapper<BUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BUser::getUsername, userNameLoginParam.getUsername())
                .select(BUser::getId, BUser::getPassword);
        BUser bUser = bUserMapper.selectOne(queryWrapper);
        if(bUser == null){
            return Result.failed("用户名或密码错误");
        }
        if (!bCryptPasswordEncoder.matches(userNameLoginParam.getPassword(), bUser.getPassword())) {
            return Result.failed("用户名或密码错误");
        }
        return getTokenMap(bUser.getId());
    }

    //获取accessToken和refreshToken
    public Result getTokenMap(Long userId) {
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", jwtUtil.createJWT(String.valueOf(userId)));
        tokenMap.put("refreshToken", jwtUtil.createJWT(String.valueOf(userId), JwtUtil.JWT_TTL*7));
        return Result.success(tokenMap, "登录成功");
    }

    @Override
    public Result emailLogin(EmailLoginParam emailLoginParam) {
        LambdaQueryWrapper<BUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BUser::getEmail, emailLoginParam.getEmail())
                .select(BUser::getId, BUser::getPassword);
        BUser bUser = bUserMapper.selectOne(queryWrapper);
        if(bUser == null){
            return Result.failed("邮箱或密码错误");
        }
        if (!bCryptPasswordEncoder.matches(emailLoginParam.getPassword(), bUser.getPassword())) {
            return Result.failed("邮箱或密码错误");
        }
        return getTokenMap(bUser.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result sign(SignParam signParam) {
        LambdaQueryWrapper<BUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BUser::getUsername, signParam.getUsername());
        BUser bUser = bUserMapper.selectOne(queryWrapper);
        if(bUser != null){
            return Result.failed("该账号已注册");
        }
        queryWrapper.clear();
        queryWrapper.eq(BUser::getEmail, signParam.getEmail());
        bUser = bUserMapper.selectOne(queryWrapper);
        if(bUser != null){
            return Result.failed("该邮箱已注册");
        }
        if (!mailCodeUtil.checkCode(signParam.getEmail(), signParam.getCode())) {
            return Result.failed("验证码错误");
        }
        BUser user = new BUser();
        user.setUsername(signParam.getUsername())
                .setPassword(bCryptPasswordEncoder.encode(signParam.getPassword()))
                .setEmail(signParam.getEmail());
        bUserMapper.insert(user);
        BUserInfo bUserInfo = new BUserInfo();
        bUserInfo.setId(user.getId());
        bUserInfoMapper.insert(bUserInfo);
        return Result.success("注册成功");
    }

    @Override
    public Result getCode(String email) throws JsonProcessingException, InterruptedException {
        if(bUserMapper.selectOne(new LambdaQueryWrapper<BUser>().eq(BUser::getEmail, email)) != null){
            return Result.failed("该邮箱已注册");
        }
        mailCodeUtil.sendCode(email);
        return Result.success("验证码已发送");
    }

    @Override
    public Result changePassword(ChangePasswordParam changePasswordParam) {
        LambdaQueryWrapper<BUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BUser::getUsername, changePasswordParam.getUsername());
        BUser bUser = bUserMapper.selectOne(queryWrapper);
        if(bUser == null){
            return Result.failed("账号不存在");
        }
        if (!bCryptPasswordEncoder.matches(changePasswordParam.getPassword(), bUser.getPassword())) {
            return Result.failed("原密码错误");
        }
        bUser.setPassword(bCryptPasswordEncoder.encode(changePasswordParam.getNewPassword()));
        bUserMapper.updateById(bUser);
        return Result.success("修改成功");
    }

    @Override
    public Result forgetPassword(ForgetPassWordParam forgetPassWordParam) {
        LambdaQueryWrapper<BUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BUser::getUsername, forgetPassWordParam.getUsername());
        BUser bUser = bUserMapper.selectOne(queryWrapper);
        if(bUser == null){
            return Result.failed("账号不存在");
        }
        if (!mailCodeUtil.checkCode(bUser.getEmail(), forgetPassWordParam.getCode())) {
            return Result.failed("验证码错误");
        }
        bUser.setPassword(bCryptPasswordEncoder.encode(forgetPassWordParam.getNewPassword()));
        bUserMapper.updateById(bUser);
        return Result.success("修改成功");
    }

    @Override
    public Result quit(Long userId) {
        redisCache.setCacheObject(WebRedisConstants.USER_LOGIN_BLACKLIST_KEY + userId, 1,
                WebRedisConstants.USER_LOGIN_BLACKLIST_TTL, TimeUnit.SECONDS);
        return Result.success("退出成功");
    }

    @Override
    public Result refreshToken(Long userId) {
        return getTokenMap(userId);
    }

    @Override
    public Result updateNickname(String nickname, Long userId) {
        BUserInfo bUserInfo = new BUserInfo();
        bUserInfo.setId(userId).setNickname(nickname);
        return Result.success("修改成功");
    }

    @Override
    public Result updateAvatarImg(String avatarImgSrc, Long userId) {
        return null;
    }

    @Override
    public Result getUploadAvatarImgSTS(String fileSuffix) {
        return null;
    }

}
