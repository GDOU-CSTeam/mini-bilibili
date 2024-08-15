package com.bili.task;

import com.bili.common.utils.AliyunOss;
import com.bili.pojo.constant.user.WebRedisConstants;
import jakarta.annotation.Resource;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CleanOssTask {

    @Resource
    RedisCache redisCache;
    @Resource
    AliyunOss aliyunOss;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public void cleanOss() {
        long now = System.currentTimeMillis();
        Objects.requireNonNull(stringRedisTemplate.opsForZSet().rangeByScore(WebRedisConstants.FILE_UPLOAD_RECORD_KEY,
                        0, now)).forEach(key -> {
            aliyunOss.deleteFile(key);
            stringRedisTemplate.opsForZSet().remove("ossStsUrl", key);
        });
    }
}
