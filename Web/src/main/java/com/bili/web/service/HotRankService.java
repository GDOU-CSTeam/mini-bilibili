package com.bili.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class HotRankService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 获取热度排行榜
    public List<String> getHotRank() {
        // 获取热度排名前 10 的视频
        Set<ZSetOperations.TypedTuple<Object>> rankedVideos = redisTemplate.opsForZSet().reverseRangeWithScores("hot:rank", 0, 9);

        List<String> topVideos = new ArrayList<>();
        for (ZSetOperations.TypedTuple<Object> video : rankedVideos) {
            topVideos.add((String) video.getValue()); // 获取视频ID
        }
        return topVideos;
    }
}
