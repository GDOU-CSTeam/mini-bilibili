package com.sky.dynamic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.sky.common","com.sky.dynamic"})
public class DynamicApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamicApplication.class, args);
    }
}