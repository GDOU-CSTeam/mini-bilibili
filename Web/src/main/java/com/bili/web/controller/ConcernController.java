package com.bili.web.controller;

import com.bili.common.dto.PageSelectWithIdParam;
import com.bili.common.utils.Result;
import com.bili.web.service.ConcernService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Concern")
@Tag(name = "Concern", description = "用户关注接口")
@Validated
public class ConcernController {

    @Resource
    ConcernService concernService;

    @Operation(summary = "关注用户")
    @PostMapping("/concernUser")
    public Result concernUser(@NotBlank @RequestParam Long concernUserId) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return concernService.concernUser(userId, concernUserId);
    }

    @Operation(summary = "取消关注")
    @DeleteMapping("/cancelConcern")
    public Result cancelConcern(@NotBlank @RequestParam Long concernUserId) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName());
        return concernService.cancelConcern(userId, concernUserId);
    }

    @Operation(summary = "分页获取关注列表")
    @GetMapping("/getConcernList")
    public Result getConcernList(@ParameterObject @Validated PageSelectWithIdParam pageSelectWithIdParam) {
        return concernService.getConcernList(pageSelectWithIdParam);
    }

    @Operation(summary = "分页获取粉丝列表")
    @GetMapping("/getFansList")
    public Result getFansList(@ParameterObject @Validated PageSelectWithIdParam pageSelectWithIdParam) {
        return concernService.getFansList(pageSelectWithIdParam);
    }
}
