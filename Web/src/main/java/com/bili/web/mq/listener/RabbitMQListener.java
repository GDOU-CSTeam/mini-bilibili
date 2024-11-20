package com.bili.web.mq.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQListener {

    @RabbitListener(queues = "user.behavior.queue")
    public void processUserBehavior(String behaviorMessage) {
        // 解析消息，处理用户行为，更新标签概率
    }
}