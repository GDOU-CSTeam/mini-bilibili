package com.bili.web.controller;

import com.bili.common.dto.PageSelectWithIdParam;
import com.bili.common.utils.Result;
import com.bili.common.utils.UserDTOHolder;
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
@RequestMapping("/concern")
@Tag(name = "用户关注接口")
@Validated
public class ConcernController {

    @Resource
    ConcernService concernService;

    @Operation(summary = "关注用户")
    @PostMapping("/concernUser")
    public Result concernUser(@NotBlank @RequestParam Long concernUserId) {
        Long userId = UserDTOHolder.getUserDTO().getId();
        return concernService.concernUser(userId, concernUserId);
    }

    @Operation(summary = "取消关注")
    @DeleteMapping("/cancelConcern")
    public Result cancelConcern(@NotBlank @RequestParam Long concernUserId) {
        Long userId = UserDTOHolder.getUserDTO().getId();
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

    @Operation(summary = "获取关注状态")
    @GetMapping("/getConcernStatus")
    public Result getConcernStatus(@NotBlank @RequestParam Long concernUserId) {
        Long userId = UserDTOHolder.getUserDTO().getId();
        return concernService.getConcernStatus(userId, concernUserId);
    }

    @Operation(summary = "获取共同关注列表")
    @GetMapping("/getCommonConcernList")
    public Result getCommonConcernList(@NotBlank @RequestParam Long concernUserId) {
        Long userId = UserDTOHolder.getUserDTO().getId();
        return concernService.getCommonConcernList(userId, concernUserId);
    }
}
