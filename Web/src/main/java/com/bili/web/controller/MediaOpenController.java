package com.bili.web.controller;

import cn.hutool.core.util.StrUtil;
import com.bili.common.utils.Result;
import com.bili.pojo.entity.MediaFiles;
import com.bili.web.service.MediaFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/open")
@Tag(name = "媒资文件预览接口")
public class MediaOpenController {

    @Autowired
    private MediaFileService mediaFileService;

    @Operation(summary = "预览文件")
    @GetMapping("/preview/{mediaId}")
    public Result getPlayUrlByMediaId(@PathVariable String mediaId) {
        //查询媒资文件信息
        MediaFiles mediaFiles = mediaFileService.getFileById(mediaId);

        if (mediaFiles == null) {
            return Result.failed("找不到资源");
        }
        //取出视频播放地址
        String url = mediaFiles.getUrl();
        if (StrUtil.isEmpty(url)) {
            return Result.failed("该资源暂不支持访问");
        }
        return Result.success(mediaFiles.getUrl());
    }
}
