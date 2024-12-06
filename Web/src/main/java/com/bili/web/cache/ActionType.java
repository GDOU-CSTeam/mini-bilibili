package com.bili.web.cache;

public class ActionType {
    public static final Integer VISIT = 1;     // 访问
    public static final Integer LIKE = 2;      // 点赞
    public static final Integer FAVORITE = 3;  // 收藏
    public static final Integer COIN = 4;      // 投币
    public static final Integer SHARE = 5;     // 分享
    public static final Integer SIGN_IN = 6;   // 签到

    // 私有构造函数防止实例化
    private ActionType() {
    }
}
