package com.bili.web.controller;

import com.aliyuncs.exceptions.ClientException;
import com.bili.common.utils.Result;
import com.bili.pojo.dto.user.GetOssStsParam;
import com.bili.web.service.OssStsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oss_sts")
@Tag(name = "OssSts", description = "OSS临时授权接口")
@Validated
public class OssStsController {

    @Resource
    OssStsService ossStsService;

    @GetMapping("/getOssSts_image")
    @Operation(summary = "获取图片上传临时授权 " +
            "type: avatar-头像 background-背景图 dynamicImage-动态图片 chatImage-聊天图片 commentImage-评论图片其中之一")
    public Result getOssStsImage(@ParameterObject @Validated GetOssStsParam getOssStsParam) throws ClientException {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ossStsService.getOssStsImage(userId, getOssStsParam);
    }

    @GetMapping("/getOssSts_video")
    @Operation(summary = "获取视频上传临时授权")
    public Result getOssStsVideo(@NotBlank @RequestParam String suffix) throws ClientException {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return ossStsService.getOssStsVideo(userId, suffix);
    }
}
