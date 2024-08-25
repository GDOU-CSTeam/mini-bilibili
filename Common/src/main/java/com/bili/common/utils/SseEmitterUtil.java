package com.bili.common.utils;

import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class SseEmitterUtil {

    //存放用户与SseEmitter的对应关系
    private final static Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    private final static ConcurrentHashMap<String, Timer> heartPool = new ConcurrentHashMap<>();

    public static SseEmitter connect(String userId) {
        // 设置超时时间，0表示不过期。默认30S，超时时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 注册回调
        /*sseEmitter.onCompletion(completionCallBack(userId));*/
        sseEmitter.onError(errorCallBack(userId));
        sseEmitter.onTimeout(timeoutCallBack(userId));
        sseEmitterMap.put(userId, sseEmitter);
        startHeartbeat(sseEmitter,userId);
        return sseEmitter;
    }

    //发送消息
    public static void sendMessage(String userId, String sseMessage) {
        try {
            SseEmitter sseEmitter = sseEmitterMap.get(userId);
            if (sseEmitter == null) {
                return;
            }
            sseEmitter.send(sseMessage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //群发消息
    public static void batchSendMessage(String message, List<String> ids) {
        ids.forEach(userId -> sendMessage(userId, message));
    }

    //群发所有人消息
    public static void batchSendMessage(String message) {
        sseEmitterMap.forEach((k, v) -> {
            try {
                v.send(message, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    //移除用户
    public static void removeUser(String userId) {
        sseEmitterMap.get(userId).complete();
        sseEmitterMap.remove(userId);
    }

    //获取当前连接数
    public static int getUserCount() {
        return sseEmitterMap.size();
    }

    /*private static Runnable completionCallBack(String userId) {
        return () -> {
        };
    }*/

    private static Runnable timeoutCallBack(String userId) {
        return () -> {
            removeUser(userId);
        };
    }

    private static Consumer<Throwable> errorCallBack(String userId) {
        return throwable -> {
            removeUser(userId);
        };
    }

    //开启心跳
    public static void startHeartbeat(SseEmitter sseemitter, String userId) {
        Timer heartbeatTimer = new Timer();
        heartPool.put(userId,heartbeatTimer);
        heartbeatTimer.schedule(new TimerTask() {
            @SneakyThrows
            @Override
            public void run() {
                if (Objects.nonNull(heartPool.get(userId))) {
                    // 发送心跳:保持长连接
                    sseemitter.send("ping");
                }
            }
        }, 25000, 25000);
    }

    //关闭心跳
    public static void stopHeartbeat(String userId) {
        Timer timer = heartPool.get(userId);
        if (timer!= null)
            timer.cancel();
        heartPool.remove(userId);
    }
}