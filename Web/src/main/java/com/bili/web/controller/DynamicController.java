package com.bili.web.controller;

import com.bili.common.utils.Result;
import com.bili.pojo.dto.user.PublishDynamicParam;
import com.bili.web.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lin
 * @since 2024-08-07 08:44:50
 */
@RestController
@RequestMapping("/dynamic")
@Tag(name = "dynamic" ,description = "动态相关接口")
@Validated
public class DynamicController {

    @Resource
    DynamicService dynamicService;

    //发布动态
    @PostMapping("/publish")
    @Operation(summary = "发布动态")
    public Result publish(@Validated @RequestBody PublishDynamicParam publishDynamicParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return dynamicService.publishToMq(userId ,publishDynamicParam);
    }
}
