package com.bili.common.utils;

import com.bili.pojo.constant.RedisConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class MailCodeUtil {
    @Resource
    MailUtil mailUtil;
    @Resource
    RedissonClient redissonClient;
    @Resource
    RedisCache redisCache;

    public Result sendCode(String MAIL_TO) throws JsonProcessingException, InterruptedException {
        RLock rlock = redissonClient.getLock("CodeLock:" + MAIL_TO);
        boolean getLock = rlock.tryLock(0,60, TimeUnit.SECONDS);
        if(!getLock){
            return Result.failed("请勿频繁发送验证码");
        }
        String MAIL_SUBJECT = "验证码";
        int code = CodeGen.generatedCode(6);
        String MAIL_TEXT = "您的验证码为："+code+"，请在5分钟内完成验证。";
        mailUtil.send(MAIL_TO,MAIL_SUBJECT,MAIL_TEXT);
        redisCache.setCacheObject(RedisConstants.LOGIN_CODE_KEY + MAIL_TO, String.valueOf(code),300L,TimeUnit.SECONDS);
        return Result.success("验证码已发送");
    }

    public boolean checkCode(String userid, String code) {;
        //判断验证码是否正确
        String realCode = redisCache.getCacheObject(RedisConstants.LOGIN_CODE_KEY + userid);
        if (realCode == null)
            return false;
        return Objects.equals(realCode, code);
    }
}
