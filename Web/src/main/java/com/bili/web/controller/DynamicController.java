package com.bili.web.controller;

import com.bili.common.dto.PageSelectParam;
import com.bili.common.dto.PageSelectWithIdParam;
import com.bili.common.utils.Result;
import com.bili.common.utils.UserDTOHolder;
import com.bili.pojo.dto.PublishDynamicParam;
import com.bili.web.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
@Tag(name = "动态相关接口")
@Validated
public class DynamicController {

    @Resource
    DynamicService dynamicService;

    //发布动态
    @PostMapping("/publish")
    @Operation(summary = "发布动态")
    public Result publish(@Validated @RequestBody PublishDynamicParam publishDynamicParam) {
        Long userId = UserDTOHolder.getUserDTO().getId();
        return dynamicService.publishToMq(userId ,publishDynamicParam);
    }

    //获取关注用户动态
    @GetMapping("/get_concern_user_dynamic")
    @Operation(summary = "获取关注用户动态")
    public Result getConcernUserDynamic(@Validated @ParameterObject PageSelectParam pageSelectParam) {
        Long userId = UserDTOHolder.getUserDTO().getId();
        return dynamicService.getConcernUserDynamic(userId, pageSelectParam);
    }

    //获取用户动态
    @GetMapping("/get_user_dynamic")
    @Operation(summary = "获取用户动态")
    public Result getUserDynamic(@Validated @ParameterObject PageSelectWithIdParam pageSelectWithIdParam) {
        return dynamicService.getUserDynamic(pageSelectWithIdParam);
    }

    //删除动态
    @DeleteMapping("/delete")
    @Operation(summary = "删除动态")
    public Result delete(@RequestParam @NotBlank String dynamicId) {
        Long userId = UserDTOHolder.getUserDTO().getId();
        return dynamicService.delete(userId, Long.valueOf(dynamicId));
    }
}
