package com.bili.web.mq.consumer;

import com.bili.web.mq.bo.PublishDynamicToMq;
import com.bili.web.mq.config.RabbitConfig;
import com.bili.web.service.DynamicService;
import com.google.gson.Gson;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.DYNAMIC_PUBLISH_QUEUE)
public class DynamicPublishConsumer {

    @Resource
    DynamicService dynamicService;
    @Resource
    Gson gson;

    @RabbitHandler
    public void process(String json) {
        dynamicService.publish(gson.fromJson(json, PublishDynamicToMq.class));
    }
}
