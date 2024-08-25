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
 * 视频基本信息表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@Schema(name = "Videos", description = "视频基本信息表")
public class Videos {

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(description = "上传视频的用户")
    private Integer userId;

    @Schema(description = "视频封面")
    private String coverImage;

    @Schema(description = "视频标题")
    private String title;

    @Schema(description = "视频类型（0自制 1转载）")
    private Byte type;

    @Schema(description = "视频所属的分区")
    private Integer sectionId;

    @Schema(description = "视频简介")
    private String description;

    @Schema(description = "视频的状态（0待审核、1已发布、2被限流、3已删除）")
    private Integer statusId;

    @Schema(description = "视频时长（秒）")
    private Integer duration;

    @Schema(description = "视频路径")
    private String videoPath;

    @Schema(description = "观看次数")
    private Integer viewCount;

    @Schema(description = "点赞人数")
    private Integer likes;

    private Integer collections;

    @Schema(description = "投币数量")
    private Integer coins;

    private Integer shares;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
