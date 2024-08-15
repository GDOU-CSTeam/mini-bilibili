package com.bili.web.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String DYNAMIC_PUBLISH_QUEUE = "DynamicPublishQueue";

    @Bean
    Queue DynamicPublishQueue() {
        return new Queue(DYNAMIC_PUBLISH_QUEUE,true);
    }

    @Bean
    Binding bindingDynamicPublishQueue(@Qualifier(DYNAMIC_PUBLISH_QUEUE) Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DYNAMIC_PUBLISH_QUEUE);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }
}

