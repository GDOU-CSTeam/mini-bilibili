package com.bili.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@SpringBootConfiguration
@EnableAspectJAutoProxy(exposeProxy = true) //用来暴露代理对象
@EnableScheduling
@EnableAsync
@ComponentScan(
        {"com.bili.common", "com.bili.web"})
@MapperScan({"com.bili.web.mapper"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}