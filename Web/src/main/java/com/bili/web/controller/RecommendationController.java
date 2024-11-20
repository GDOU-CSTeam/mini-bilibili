package com.bili.web.controller;

import com.bili.web.service.VideoRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private VideoRecommendationService videoRecommendationService;

    // 获取推荐视频
    @GetMapping("/{userId}")
    public List<String> getRecommendations(@PathVariable String userId) {
        Set<String> viewedVideos = new HashSet<>(); // 假设前端传过来浏览过的视频集合
        return videoRecommendationService.recommendVideos(userId, viewedVideos);
    }
}
