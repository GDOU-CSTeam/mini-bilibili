package com.bili.web.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bili.common.utils.*;
import com.bili.pojo.constant.user.WebOssConstants;
import com.bili.pojo.constant.user.WebRedisConstants;
import com.bili.pojo.dto.user.*;
import com.bili.pojo.entity.user.BUser;
import com.bili.pojo.entity.user.BUserInfo;
import com.bili.pojo.mapper.user.BUserInfoMapper;
import com.bili.pojo.mapper.user.BUserMapper;
import com.bili.web.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    @Resource
    AliyunOss aliyunOss;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result signIn(Long userId) {
        if(redisCache.getCacheObject(WebRedisConstants.USER_TODAY_SIGN_IN_KEY + userId) != null){
            return Result.failed("今日已签到");
        }
        //获取现在时间
        LocalDateTime end = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        long seconds = ChronoUnit.SECONDS.between(LocalDateTime.now(),end);
        LambdaQueryWrapper<BUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BUserInfo::getId, userId)
                .select(BUserInfo::getId, BUserInfo::getCoins, BUserInfo::getExperience)
                .last("limit 1");
        BUserInfo bUserInfo = bUserInfoMapper.selectOne(queryWrapper);
        bUserInfo.setCoins(bUserInfo.getCoins() + 1)
                .setExperience(bUserInfo.getExperience() + 10);
        bUserInfoMapper.updateById(bUserInfo);
        redisCache.setCacheObject(WebRedisConstants.USER_TODAY_SIGN_IN_KEY + userId,
                1 , seconds, TimeUnit.SECONDS);
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
        return getTokenMap(bUser.getId(), "登录成功");
    }

    //获取accessToken和refreshToken
    public Result getTokenMap(Long userId, String ResultMessage) {
        HashMap<String, String> tokenMap = new HashMap<>();
        tokenMap.put("accessToken", jwtUtil.createJWT(String.valueOf(userId)));
        tokenMap.put("refreshToken", jwtUtil.createJWT(String.valueOf(userId), jwtUtil.jwtTtl * 7));
        return Result.success(tokenMap, ResultMessage);
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
        return getTokenMap(bUser.getId(), "登录成功");
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
        bUserInfo.setNickname("用户" + user.getId());
        bUserInfo.setAvatar(WebOssConstants.DEFAULT_USER_AVATAR);
        bUserInfoMapper.insert(bUserInfo);
        return Result.success("注册成功");
    }

    @Override
    public Result getCode(String email) throws JsonProcessingException, InterruptedException {
        try {
            mailCodeUtil.sendCode(email);
        }
        catch (Exception e){
            return Result.failed("验证码发送失败");
        }
        return Result.success("验证码已发送");
    }

    @Override
    public Result changePassword(ChangePasswordParam changePasswordParam) {
        LambdaQueryWrapper<BUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BUser::getUsername, changePasswordParam.getUsername())
                .select(BUser::getId, BUser::getPassword)
                .last("limit 1");
        BUser bUser = bUserMapper.selectOne(queryWrapper);
        if(bUser == null){
            return Result.failed("账号不存在");
        }
        if (!bCryptPasswordEncoder.matches(changePasswordParam.getPassword(), bUser.getPassword())) {
            return Result.failed("原密码错误");
        }
        if (changePasswordParam.getNewPassword().equals(changePasswordParam.getPassword())) {
            return Result.failed("新密码不能与原密码相同");
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
        redisCache.setCacheObject(WebRedisConstants.USER_LOGIN_BLACKLIST_KEY + userId, System.currentTimeMillis(),
                jwtUtil.jwtTtl, TimeUnit.SECONDS);
        return Result.success("退出成功");
    }

    @Override
    public Result refreshToken(Long userId) {
        return getTokenMap(userId, null);
    }

    @Override
    public Result updateUserInfo(Long userId, UpdateUserInfoParam updateUserInfoParam) {
        if (updateUserInfoParam.getSex() != null && updateUserInfoParam.getSex() != 0 && updateUserInfoParam.getSex() != 1) {
            return Result.failed("性别参数错误");
        }
        if (updateUserInfoParam.getBjCover() != null && aliyunOss.findFile(updateUserInfoParam.getBjCover())){
            return Result.failed("个人中心背景图未上传");
        }
        if (updateUserInfoParam.getAvatar() != null && aliyunOss.findFile(updateUserInfoParam.getAvatar())){
            return Result.failed("头像未上传");
        }
        if (updateUserInfoParam.getNickname() != null && updateUserInfoParam.getNickname().length() > 25){
            return Result.failed("昵称过长");
        }
        if (updateUserInfoParam.getIntro() != null && updateUserInfoParam.getIntro().length() > 255){
            return Result.failed("简介过长");
        }
        BUserInfo bUserInfo = new BUserInfo();
        bUserInfo.setId(userId)
                .setNickname(updateUserInfoParam.getNickname())
                .setAvatar(updateUserInfoParam.getAvatar())
                .setIntro(updateUserInfoParam.getIntro())
                .setSex(updateUserInfoParam.getSex())
                .setBjCover(updateUserInfoParam.getBjCover());
        bUserInfoMapper.updateById(bUserInfo);
        return Result.success("更新成功");
    }

}
