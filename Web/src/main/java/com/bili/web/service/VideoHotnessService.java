   package com.bili.web.service;

   import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
   import com.bili.pojo.entity.Video;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.data.redis.core.RedisTemplate;
   import org.springframework.data.redis.core.ZSetOperations;
   import org.springframework.stereotype.Service;

   import java.time.Instant;
   import java.time.ZoneId;
   import java.util.Iterator;
   import java.util.List;

   @Service
   public class VideoHotnessService {

       private static final double HALF_LIFE = 86400; // 半衰期：24小时（86400秒）

       @Autowired
       private VideoService videoService; // 使用 MyBatisPlus 或 JPA 查询视频
       @Autowired
       private RedisTemplate<String, Object> redisTemplate;

       // 权重常量（根据实际需求调整）
       private static final double LIKE_WEIGHT = 0.4;
       private static final double VIEW_WEIGHT = 0.3;
       private static final double SHARE_WEIGHT = 0.2;
       private static final double COLLECT_WEIGHT = 0.1;

       // 计算视频热度
       public double calculateVideoHotness(Video video) {
           long likes = Long.parseLong(video.getLikes());  // 点赞数
           long views = video.getViewCount();  // 浏览量
           long shares = Long.parseLong(video.getShares()); // 分享数
           long collects = Long.parseLong(video.getCollections()); // 收藏数

           // 当前时间 - 视频发布时间的时间差 (秒)
           long timeDifference = Instant.now().getEpochSecond() - video.getCreateTime().toEpochSecond(ZoneId.systemDefault().getRules().getOffset(Instant.now()));

           // 半衰期公式
           double timeFactor = Math.pow(0.5, (double) timeDifference / HALF_LIFE);

           // 计算权重
           double weight = (likes * LIKE_WEIGHT) + (views * VIEW_WEIGHT) + (shares * SHARE_WEIGHT) + (collects * COLLECT_WEIGHT);

           // 总热度 = 权重 * 时差系数
           return weight * timeFactor;
       }

       // 更新视频热度到 Redis
       public void updateHotVideoRank(Video video) {
           double hotness = calculateVideoHotness(video);

           // 更新 Redis ZSet
           redisTemplate.opsForZSet().add("hot:rank", video.getId().toString(), hotness);

           // 将视频加入热门视频集合
           if (hotness > 50) {  // 热度阈值设定为50
               redisTemplate.opsForSet().add("hot:video", video.getId().toString());
           }
       }

       // 执行定时任务，更新所有视频的热度
       public void updateAllVideosHotness() {
           // 假设你有方法获取所有视频，实际使用分页查询
           List<Video> allVideos = videoService.list(new LambdaUpdateWrapper<>());

           for (Video video : allVideos) {
               updateHotVideoRank(video);
           }
       }
   }
   