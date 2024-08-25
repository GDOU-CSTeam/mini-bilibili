package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 视频评论表
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("video_comments")
public class VideoComments implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Null(groups = ValidGroup.Create.class)
    @Schema(description = "主键ID", example = "1")
    private Integer id;

    /**
     * 评论的视频
     */
    @Min(value = 0)
    @Schema(description = "评论的视频ID", example = "1")
    private Integer videoId;

    /**
     * 评论的用户
     */
    @Null(groups = ValidGroup.Create.class)
    @Schema(description = "评论的用户ID", example = "1")
    private Integer userId;

    /**
     * 此评论的父评论（一级评论值为0）
     */
    @Min(value = 0)
    @Schema(description = "父评论ID", example = "0")
    private Integer parentId;

    /**
     * 回复的用户id
     */
    @Min(value = 0)
    @Schema(description = "回复的用户ID", example = "1")
    private Integer replyUserId;

    /**
     * 回复的评论id
     */
    @Min(value = 0)
    @Schema(description = "回复的评论ID", example = "1")
    private Integer replyCommentId;

    /**
     * 评论的内容
     */
    @Size(min = 1, max = 1000)
    @Schema(description = "评论内容", example = "这是一个很好的视频！")
    private String content;

    /**
     * 点赞数
     */
    @Null(groups = ValidGroup.Create.class)
    @Schema(description = "点赞数", example = "10")
    private Integer likes;

    /**
     * 评论的图片
     */
    @Schema(description = "评论图片URL", example = "https://example.com/image.jpg")
    private String images;

    /**
     * 创建时间
     */
    @Null(groups = ValidGroup.Create.class)
    @Schema(description = "创建时间", example = "2024-08-16T12:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Null(groups = ValidGroup.Create.class)
    @Schema(description = "更新时间", example = "2024-08-16T12:00:00")
    private LocalDateTime updateTime;

    /**
     * 删除标志（0代表未删除，1代表已删除）
     */
    @Null(groups = ValidGroup.Create.class)
    @Schema(description = "删除标志", example = "0")
    private Integer delFlag;

}
