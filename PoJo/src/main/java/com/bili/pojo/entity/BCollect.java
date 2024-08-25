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
 * 视频收藏表
 * </p>
 *
 * @author lin
 * @since 2024-08-20 08:06:19
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_collect")
@Schema(name = "BCollect", description = "视频收藏表")
public class BCollect {

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "视频id")
    private Long videoId;

    @Schema(description = "收藏时间")
    private LocalDateTime createTime;
}
