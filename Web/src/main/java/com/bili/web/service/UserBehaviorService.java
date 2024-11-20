package com.bili.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserBehaviorService {

    @Autowired
    private RedisUserModelService redisUserModelService;

    private double maxProbability = 1.0; // 最大概率

    // 增加或减少标签概率
    public void updateTagProbabilityBasedOnBehavior(String userId, List<String> videoTags, double delta) {
        // 遍历视频标签并更新用户模型库中的标签概率
        for (String tagId : videoTags) {
            redisUserModelService.updateUserTagProbability(userId, tagId, delta);
        }

        // 调整概率以避免膨胀，归一化处理
        normalizeUserTagProbabilities(userId);
    }

    // 归一化标签概率，避免膨胀
    private void normalizeUserTagProbabilities(String userId) {
        Map<String, Double> tagProbabilities = redisUserModelService.getUserTagsProbability(userId);

        double totalProbability = tagProbabilities.values().stream().mapToDouble(Double::doubleValue).sum();
        double normalizationFactor = totalProbability / 1.0; // 确保总和为 1

        for (Map.Entry<String, Double> entry : tagProbabilities.entrySet()) {
            double newProbability = entry.getValue() / normalizationFactor;
            newProbability = Math.min(newProbability, maxProbability); // 确保最大概率不超过 1
            redisUserModelService.updateUserTagProbability(userId, entry.getKey(), newProbability);
        }
    }
}
