package com.bili.web.task;

import com.bili.web.service.VideoHotnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HotVideoScheduler {

    @Autowired
    private VideoHotnessService videoHotnessService;

    // 每3小时更新一次所有视频热度
    @Scheduled(cron = "0 0 */3 * * ?") 
    public void updateHotVideos() {
        videoHotnessService.updateAllVideosHotness();
    }

    // 每小时更新热度排行榜
    @Scheduled(cron = "0 0 * * * ?") 
    public void updateHotVideoRankings() {
        // 执行热度排行榜更新
        // 暂时不进行额外操作，Redis 直接更新
    }
}
