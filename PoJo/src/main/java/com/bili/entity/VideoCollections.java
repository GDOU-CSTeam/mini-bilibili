package com.bili.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import reactor.util.annotation.NonNull;

/**
 * <p>
 * 视频收藏夹表
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("video_collections")

@Schema(description = "视频收藏夹实体类")
public class VideoCollections {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(description = "主键ID", example = "12345")
    @TableId(value = "id", type = IdType.AUTO)
    @Null(groups = ValidGroup.Create.class)
    @Min(value = 0, groups = ValidGroup.Update.class)
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "10001")
    @Null(groups = ValidGroup.Create.class)
    @Null(groups = ValidGroup.Update.class)
    private Long userId;

    /**
     * 收藏夹名称
     */
    @Schema(description = "收藏夹名称", example = "我的最爱")
    @NotBlank(groups = ValidGroup.Create.class)
    private String name;

    /**
     * 收藏夹简介
     */
    @Schema(description = "收藏夹简介", example = "这是我最喜欢的电影和电视剧")
    private String description;

    /**
     * 收藏夹封面图片的URL
     */
    @Schema(description = "收藏夹封面图片的URL", example = "https://example.com/collection-cover.jpg")
    private String coverImage;

    /**
     * 是否公开收藏夹
     */
    @Schema(description = "是否公开收藏夹", example = "true")
    private Boolean isPublic;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2023-09-01T12:00:00Z")
    @Null(groups = ValidGroup.Create.class)
    @Null(groups = ValidGroup.Update.class)
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "2023-09-01T12:00:00Z")
    @Null(groups = ValidGroup.Create.class)
    @Null(groups = ValidGroup.Update.class)
    private LocalDateTime updatedTime;
}
