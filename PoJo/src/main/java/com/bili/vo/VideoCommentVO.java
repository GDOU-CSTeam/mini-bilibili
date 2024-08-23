package com.bili.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class VideoCommentVO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论的视频
     */
    private Integer videoId;

    /**
     * 评论的用户
     */
    private Integer userId;

    /**
     * 此评论的父评论（一级评论值为0）
     */
    private Integer parentId;

    /**
     * 回复的用户id
     */
    private Integer replyUserId;

    /**
     * 回复的用户nickName
     */
    private String replyUserName;

    /**
     * 回复的评论id
     */
    private Integer replyCommentId;

    /**
     * 评论的内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer likes;

    /**
     * 点踩数
     */
    private Integer dislikes;

    /**
     * 评论的图片
     */
    private String images;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 子评论
     */
    private List<VideoCommentVO> children;
}
