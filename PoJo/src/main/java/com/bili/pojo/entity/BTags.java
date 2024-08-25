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
 * 博客标签表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_tags")
@Schema(name = "BTags", description = "博客标签表")
public class BTags {

    @Schema(description = "主键id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "标签名称")
    private String name;

    private Integer clickVolume;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后更新时间")
    private LocalDateTime updateTime;
}
