package com.bili.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

// Redis 系统标签库服务
@Service
public class RedisSystemTagsService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 获取标签下的视频集合
    public Set<String> getVideosByTagId(String tagId) {
        String key = "system:stock:" + tagId;
        return redisTemplate.opsForSet().members(key);
    }

    // 添加视频到标签
    public void addVideoToTag(String tagId, String videoId) {
        String key = "system:stock:" + tagId;
        redisTemplate.opsForSet().add(key, videoId);
    }
}