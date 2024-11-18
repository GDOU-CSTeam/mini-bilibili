package com.bili.web;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolTest {

    private static final AtomicInteger threadCounter = new AtomicInteger(1);  // 用于生成线程ID

    private static ExecutorService executorService = new ThreadPoolExecutor(
            5,
            5,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(100),
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    // 使用 AtomicInteger 生成唯一的线程ID
                    Thread thread = new Thread(r);
                    thread.setName("unique_thread_id-" + threadCounter.getAndIncrement());  // 为每个线程分配一个名称
                    return thread;
                }
            }
    );

    @Test
    public void main() {
        // 提交多个任务
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            executorService.submit(() -> {
                // 获取当前线程的名称
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + taskId + " is being processed by " + threadName);
            });
        }
        executorService.shutdown();
    }
}
