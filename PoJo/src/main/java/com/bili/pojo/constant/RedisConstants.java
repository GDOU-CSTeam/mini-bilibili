package com.bili.pojo.constant;

public class RedisConstants {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final String VERIFIED_CODE_KEY = "verified:code:";
    public static final Long LOGIN_CODE_TTL = 5L;
    public static final Long VERIFIED_CODE_TTL = 5L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L; //原来30min

    public static final Long CACHE_NULL_TTL = 2L;
    public static final Long COIN_PAUSE_TTL = 24 * 60L;

    public static final Long CACHE_SHOP_TTL = 30L;
    public static final String CACHE_SHOP_KEY = "cache:shop:";

    public static final String LOCK_SHOP_KEY = "lock:shop:";
    public static final Long LOCK_SHOP_TTL = 10L;

    public static final String SECKILL_STOCK_KEY = "seckill:stock:";
    public static final String VIDEO_LIKE_KEY = "video:like:";
    public static final String VIDEO_COIN_KEY = "video:coin:";
    public static final String VIDEO_SHARE_KEY = "video:share:";
    public static final String VIDEO_COLLECTION_KEY = "video:collection:";
    public static final String FEED_KEY = "feed:";
    public static final String SHOP_GEO_KEY = "shop:geo:";

    //每日奖励
    public static final String USER_SIGN_KEY = "rewards:sign:";
    public static final String USER_VIDEO_KEY = "rewards:video:";
    public static final String USER_COIN_KEY = "rewards:coin:";
    public static final String USER_SHARE_KEY = "rewards:share:";


    public static final String USER_COLLECTION_KEY = "user:collection:";
    public static final String COLLECTION_LIKE_KEY = "comment:like:";
}
