package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 视频评论表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@Schema(name = "Comments", description = "视频评论表")
public class Comments {

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "评论的视频")
    private Integer videoId;

    @Schema(description = "评论的用户")
    private Integer userId;

    @Schema(description = "此评论的父评论（顶级评论此字段为 NULL）")
    private Integer parentId;

    @Schema(description = "评论的内容")
    private String content;

    @Schema(description = "评论的创建时间")
    private LocalDateTime createdTime;

    @Schema(description = "评论的更新时间")
    private LocalDateTime updatedTime;

    @Schema(description = "评论的状态")
    private Byte statusId;
}
