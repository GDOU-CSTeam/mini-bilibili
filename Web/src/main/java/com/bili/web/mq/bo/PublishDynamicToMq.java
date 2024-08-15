package com.bili.web.mq.bo;

import com.bili.pojo.dto.user.PublishDynamicParam;
import lombok.Data;

/**
 * 表示用于向消息队列发送动态发布的数据传输对象。
 * 该类封装了发送动态发布的用户ID及详细参数。
 */
@Data
public class PublishDynamicToMq {

    // 用户ID，标识动态发布的发送者。
    Long userId;

    // PublishDynamicParam 对象，包含动态发布发送的详细参数。
    PublishDynamicParam publishDynamicParam;
}
