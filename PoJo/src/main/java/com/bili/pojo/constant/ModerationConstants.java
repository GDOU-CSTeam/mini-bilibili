package com.bili.pojo.constant;

public class ModerationConstants {

    // ------------------- 视频相关常量 -------------------

    /**
     * 通用基线检测（视频）
     */
    public static final String VIDEO_DETECTION = "videoDetection";


    // ------------------- 图像相关常量 -------------------

    /**
     * 通用基线检测（图像）
     */
    public static final String IMAGE_BASELINE_CHECK = "baselineCheck";

    /**
     * 内容治理检测（图像）
     */
    public static final String IMAGE_TONALITY_IMPROVE = "tonalityImprove";

    /**
     * AIGC图像检测
     */
    public static final String IMAGE_AIGC_CHECK = "aigcCheck";



    // ------------------- 文本相关常量 -------------------

    /**
     * 用户昵称检测
     */
    public static final String TEXT_NICKNAME_DETECTION = "nickname_detection";

    /**
     * 私聊互动内容检测
     */
    public static final String TEXT_CHAT_DETECTION = "chat_detection";

    /**
     * 公聊评论内容检测
     */
    public static final String TEXT_COMMENT_DETECTION = "comment_detection";

    /**
     * AIGC文字检测
     */
    public static final String TEXT_AI_ART_DETECTION = "ai_art_detection";

    /**
     * 广告法合规检测
     */
    public static final String TEXT_AD_COMPLIANCE_DETECTION = "ad_compliance_detection";

    /**
     * PGC教学物料检测
     */
    public static final String TEXT_PGC_DETECTION = "pgc_detection";

    /**
     * URL风险链接检测
     */
    public static final String TEXT_URL_DETECTION = "url_detection";
}
