package com.bili.web.controller;


import com.bili.common.utils.Result;
import com.bili.pojo.entity.VideoComments;
import com.bili.web.service.IVideoCommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 视频评论表 前端控制器
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-16
 */
@Tag(name = "视频评论接口")
@RestController
@RequestMapping("/video-comments")
public class VideoCommentsController {

    @Autowired
    private IVideoCommentsService commentsService;

    @Operation(summary = "分页查询视频评论")
    @GetMapping("/page")
    public Result pageComment(@Schema(description = "视频id") Integer videoId,
                              @Schema(description = "当前页") Integer pageNum,
                              @Schema(description = "每页大小") Integer pageSize,
                              @Schema(description = "排序方式,1最热，2最新") Integer order){
        return commentsService.pageComment(videoId, pageNum, pageSize, order);
    }

    @Operation(summary = "添加评论")
    @PostMapping()
    public Result addComment(@RequestBody VideoComments comments){
        return commentsService.addComment(comments);
    }

    @Operation(summary = "批量删除评论")
    @DeleteMapping("/{commentIds}")
    public Result deleteComment(@PathVariable List<Integer> commentIds){
        return commentsService.deleteComment(commentIds);
    }

    @Operation(summary = "评论的点赞")
    @GetMapping("/{commentId}")
    public Result likeComment(@Schema(description = "评论id") @PathVariable Integer commentId){
        return commentsService.likeComment(commentId);
    }


}
