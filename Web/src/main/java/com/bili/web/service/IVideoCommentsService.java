package com.bili.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bili.common.utils.Result;
import com.bili.pojo.entity.VideoComments;

import java.util.List;

/**
 * <p>
 * 视频评论表 服务类
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-16
 */
public interface IVideoCommentsService extends IService<VideoComments> {

    /**
     * 分页查询视频评论
     * @param videoId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result pageComment(Integer videoId, Integer pageNum, Integer pageSize, Integer order);

    /**
     * 添加评论
     * @param comments
     * @return
     */
    Result addComment(VideoComments comments);

    /**
     * 删除评论
     * @param commentIds
     * @return
     */
    Result deleteComment(List<Integer> commentIds);

    /**
     * 评论的点赞
     * @param commentId
     * @return
     */
    Result likeComment(Integer commentId);
}
