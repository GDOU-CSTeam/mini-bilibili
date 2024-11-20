package com.bili.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// Redis 用户模型库服务
@Service
public class RedisUserModelService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 获取用户标签概率
     public Map<String, Double> getUserTagsProbability(String userId) {
           String key = "user:model:" + userId;
           Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
           Map<String, Double> result = new HashMap<>();
           for (Map.Entry<Object, Object> entry : entries.entrySet()) {
               String tag = (String) entry.getKey();
               Double probability = (Double) entry.getValue();
               result.put(tag, probability);
           }
           return result;
       }


    // 设置用户标签概率
    public void setUserTagsProbability(String userId, Map<String, Double> tagProbabilities) {
        String key = "user:model:" + userId;
        redisTemplate.opsForHash().putAll(key, tagProbabilities);
    }

    // 更新用户标签概率
    public void updateUserTagProbability(String userId, String tagId, double delta) {
        String key = "user:model:" + userId;
        redisTemplate.opsForHash().increment(key, tagId, delta);
    }
}
