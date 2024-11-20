package com.bili.web.controller;

import com.bili.web.service.HotRankService;
import com.bili.web.service.HotVideoPushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hot-videos")
public class HotVideoController {

    @Autowired
    private HotVideoPushService hotVideoPushService;

    @Autowired
    private HotRankService hotRankService;

    // 获取热门视频
    @GetMapping("/push")
    public List<String> getHotVideos() {
        return hotVideoPushService.getHotVideos();
    }

    // 获取热度排行榜
    @GetMapping("/rank")
    public List<String> getHotRank() {
        return hotRankService.getHotRank();
    }
}
