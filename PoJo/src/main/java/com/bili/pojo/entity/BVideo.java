package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 系统管理-视频信息表
 * </p>
 *
 * @author lin
 * @since 2024-08-20 08:06:20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_video")
@Schema(name = "BVideo", description = "系统管理-视频信息表")
public class BVideo {

    @Schema(description = "视频唯一标识")
      private Long id;

    @Schema(description = "视频标题")
    private String title;

    @Schema(description = "视频描述")
    private String description;

    @Schema(description = "视频文件URL或存储路径")
    private String videoUrl;

    @Schema(description = "视频缩略图URL")
    private String thumbnailUrl;

    @Schema(description = "视频时长（秒）")
    private Integer duration;

    @Schema(description = "观看次数")
    private Long viewCount;

    @Schema(description = "点赞次数")
    private Integer likeCount;

    @Schema(description = "点踩次数")
    private Integer dislikeCount;

    @Schema(description = "评论次数")
    private Integer commentCount;

    @Schema(description = "视频状态")
    private String status;

    @Schema(description = "是否公开")
    private Boolean isPublic;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "发布时间")
    private LocalDateTime publishedTime;

    @Schema(description = "上传者用户ID")
    private Long userId;

    @Schema(description = "视频分类ID")
    private Long categoryId;
}
