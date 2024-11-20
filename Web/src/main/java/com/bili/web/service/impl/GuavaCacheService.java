package com.bili.web.service.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Configuration
public class GuavaCacheService {

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Bean
     public Cache<String, String> guavaCache() {
        Cache<String, String> guavaCache = CacheBuilder.newBuilder()
            //设置并发级别为8，并发级别是指可以同时写缓存的线程数
            .concurrencyLevel(8)
            //设置缓存容器的初始容量为10
            .initialCapacity(10)
            //设置缓存最大容量为100，超过100之后就会按照LRU最近虽少使用算法来移除缓存项
            .maximumSize(100)
            //是否需要统计缓存情况,该操作消耗一定的性能,生产环境应该去除
            .recordStats()
            //设置写缓存后n秒钟过期
            .expireAfterWrite(30, TimeUnit.MINUTES)
            //设置读写缓存后n秒钟过期,实际很少用到,类似于expireAfterWrite
            //.expireAfterAccess(17, TimeUnit.SECONDS)
            //只阻塞当前数据加载线程，其他线程返回旧值
            //.refreshAfterWrite(13, TimeUnit.SECONDS)
            //设置缓存的移除通知
            .removalListener(notification -> {
                System.out.println(notification.getKey() + " " + notification.getValue() + " 被移除,原因:" + notification.getCause());
            })
            .build(CacheLoader.asyncReloading(new CacheLoader<String, String>() {
                //异步加载数据
                @Override
                public String load(String s) throws Exception {
                    System.out.println("异步加载数据");
                    Thread.sleep(5000);
                    return s + System.currentTimeMillis();
                }
            }, executorService));
        return guavaCache;
    }
}