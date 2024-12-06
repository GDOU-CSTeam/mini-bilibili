package com.bili.web.cache;

import com.bili.web.cache.ActionRecord;
import com.bili.web.service.IUserActionRecordsService;
import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

@Configuration
public class GuavaCacheConfig {

    @Autowired
    private IUserActionRecordsService userActionRecordsService;

    private static ExecutorService poolExecutor = new ThreadPoolExecutor(
            3, // 核心线程数
            5, // 最大线程数
            5000, // 线程空闲时间（单位：秒）
            TimeUnit.MINUTES,
            new LinkedBlockingDeque<>(100), // 队列容量
            Executors.defaultThreadFactory()
    );


    @Bean
    public LoadingCache<Long, ActionRecord> guavaCache() {
        LoadingCache<Long, ActionRecord> guavaCache = CacheBuilder.newBuilder()
                // 设置并发级别为 8
                .concurrencyLevel(8)
                // 设置缓存容器的初始容量为 1000
                .initialCapacity(1000)
                // 设置缓存最大容量为 10000，超过会使用 LRU 策略移除
                .maximumSize(10000)
                // 是否需要统计缓存情况，记录缓存的命中、未命中、移除等情况
                .recordStats()
                // 设置缓存项在无访问后 10 分钟过期
                .expireAfterAccess(10, TimeUnit.MINUTES)
                // 缓存项被移除时的处理逻辑
                .removalListener(new RemovalListener<Long, ActionRecord>() {
                    @Override
                    public void onRemoval(RemovalNotification<Long, ActionRecord> notification) {
                        // 无论缓存项如何移除，都进行持久化操作
                        userActionRecordsService.savaToDataBase(notification.getValue());
                    }
                })
                // 使用异步加载器进行缓存数据加载
                .build(new CacheLoader<Long, ActionRecord>() {
                    @Override
                    public ActionRecord load(Long key) throws Exception {
                        // 模拟从数据库或其他地方加载数据
                        ActionRecord actionRecord = userActionRecordsService.getFormDataBase(key);
                        return actionRecord;
                    }
                });
        return guavaCache;
    }
}
