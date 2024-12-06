package com.bili.web.mq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
@EnableRabbit  // 启用 RabbitMQ 注解支持
public class RabbitConfig {

    // 定义队列名称常量
    public static final String DYNAMIC_QUEUE = "DynamicQueue";

    // 创建队列 Bean
    @Bean
    public Queue dynamicQueue() {
        return new Queue(DYNAMIC_QUEUE, true); // 队列持久化
    }

    // 创建绑定 Bean，绑定队列和交换机，使用主题交换机（TopicExchange）
    @Bean
    public Binding bindingDynamicQueue(@Qualifier("dynamicQueue") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DYNAMIC_QUEUE); // 绑定队列与交换机
    }

    // 创建交换机 Bean
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("topicExchange"); // 创建名为 "topicExchange" 的 TopicExchange
    }

    // 创建 RabbitTemplate Bean，用于发送消息
    @Bean
    public RabbitTemplate rabbitTemplate(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 你可以在这里设置 RabbitTemplate 的其他配置
        return rabbitTemplate;
    }

    @Bean
    public Queue behaviorQueue() {
        return new Queue("user.behavior.queue", true);  // 队列持久化
    }

}

