package com.bili.controller;


import com.bili.service.IFollowService;
import com.bili.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-21
 */
@Tag(name = "用户关注模块")
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Autowired
    private IFollowService followService;

    @Operation(summary = "是否关注用户")
    @GetMapping("/or/not/{id}")
    public Result isFollow(@PathVariable("id") Long followUserId) {
        return followService.isFollow(followUserId);
    }

    @Operation(summary = "关注true和取关false")
    @PutMapping("/{id}/{isFollow}")
    public Result follow(@PathVariable("id") Long followUserId, @PathVariable("isFollow") Boolean isFollow) {
        return followService.follow(followUserId, isFollow);
    }

    @Operation(summary = "获取共同关注")
    @GetMapping("/follow/common/{id}")
    public Result followCommons(@PathVariable("id") Long followUserId) {
        return followService.followCommons(followUserId);
    }
}
