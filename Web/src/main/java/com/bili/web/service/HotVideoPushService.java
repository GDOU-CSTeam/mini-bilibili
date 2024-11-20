   package com.bili.web.service;

   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.data.redis.core.RedisTemplate;
   import org.springframework.stereotype.Service;

   import java.util.ArrayList;
   import java.util.Collections;
   import java.util.List;
   import java.util.Set;
   import java.util.stream.Collectors;

   @Service
   public class HotVideoPushService {

       @Autowired
       private RedisTemplate<String, Object> redisTemplate;

       // 获取热门视频
       public List<String> getHotVideos() {
           // 获取热门视频集合
           Set<Object> hotVideosObj = redisTemplate.opsForSet().members("hot:video");
           Set<String> hotVideos = hotVideosObj == null ? null : hotVideosObj.stream()
                   .map(Object::toString)
                   .collect(Collectors.toSet());

           // 如果视频数量少于 5，扩容
           if (hotVideos == null || hotVideos.size() < 5) {
               return getFallbackVideos(); // 拉取备用视频
           }

           // 随机打乱热门视频并返回前 10 个
           List<String> hotVideoList = new ArrayList<>(hotVideos);
           Collections.shuffle(hotVideoList);
           return hotVideoList.subList(0, Math.min(hotVideoList.size(), 10));
       }

       // 视频数量不足时拉取备用视频
       private List<String> getFallbackVideos() {
           //todo 假设从其他地方拉取备用视频
           return List.of("video1", "video2", "video3", "video4", "video5");
       }
   }
   