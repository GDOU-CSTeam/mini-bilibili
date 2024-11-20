package com.bili.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TagProbabilityService {

    @Autowired
    private RedisSystemTagsService redisSystemTagsService;

    @Autowired
    private RedisUserModelService redisUserModelService;

    // 处理用户订阅分类后的标签概率初始化
    public void subscribeCategory(String userId, List<String> categoryTags) {
        // 假设每个标签的初始概率是 1/标签数
        Map<String, Double> tagProbabilities = new HashMap<>();
        double initialProbability = 1.0 / categoryTags.size();
        
        // 将标签和初始概率存入用户模型库
        for (String tagId : categoryTags) {
            tagProbabilities.put(tagId, initialProbability);
        }
        
        // 保存到 Redis
        redisUserModelService.setUserTagsProbability(userId, tagProbabilities);
    }
}
