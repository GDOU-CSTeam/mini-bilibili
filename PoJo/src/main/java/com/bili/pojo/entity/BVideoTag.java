package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lin
 * @since 2024-08-20 08:06:20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_video_tag")
@Schema(name = "BVideoTag", description = "")
public class BVideoTag {

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "视频id")
    private Long videoId;

    @Schema(description = "标签id")
    private Long tagId;
}
