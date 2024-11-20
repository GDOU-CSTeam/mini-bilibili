package com.bili.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VideoRecommendationService {

    @Autowired
    private RedisSystemTagsService redisSystemTagsService;

    @Autowired
    private RedisUserModelService redisUserModelService;

    // 推送推荐视频
    public List<String> recommendVideos(String userId, Set<String> viewedVideos) {
        // 获取用户的标签概率
        Map<String, Double> tagProbabilities = redisUserModelService.getUserTagsProbability(userId);
        
        // 生成用户兴趣标签列表
        List<String> interestedTags = new ArrayList<>(tagProbabilities.keySet());
        
        List<String> recommendedVideos = new ArrayList<>();
        
        // 根据标签和概率推荐视频
        Random random = new Random();
        for (String tagId : interestedTags) {
            double probability = tagProbabilities.get(tagId);
            if (random.nextDouble() < probability) {
                // 获取标签下的视频
                Set<String> videoIds = redisSystemTagsService.getVideosByTagId(tagId);
                
                // 去重
                videoIds.removeAll(viewedVideos);
                
                // 随机选择一个视频进行推荐
                if (!videoIds.isEmpty()) {
                    String videoId = videoIds.iterator().next();
                    recommendedVideos.add(videoId);
                    viewedVideos.add(videoId); // 加入已浏览视频集合，避免重复推荐
                }
            }
        }

        return recommendedVideos;
    }
}
