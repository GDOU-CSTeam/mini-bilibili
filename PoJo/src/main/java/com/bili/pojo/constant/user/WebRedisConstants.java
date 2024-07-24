package com.bili.pojo.constant.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WebRedisConstants {
    // 用户今日签到key
    public static final String USER_TODAY_SIGN_IN_KEY = "TodaySignIn:";
    // 用户登录黑名单key
    public static final String USER_LOGIN_BLACKLIST_KEY = "UserLoginBlacklist:";
    // 用户登录黑名单ttl
    @Value("${jwt.ttl}")
    public static Long USER_LOGIN_BLACKLIST_TTL;
}
