package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 视频分类表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_category")
@Schema(name = "BCategory", description = "视频分类表")
public class BCategory {

    @Schema(description = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "分类名称")
    private String name;

    private Integer clickVolume;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "图标")
    private String icon;
}
