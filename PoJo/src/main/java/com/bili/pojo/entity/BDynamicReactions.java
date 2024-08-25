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
 * @since 2024-08-25 02:55:13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_dynamic_reactions")
@Schema(name = "BDynamicReactions", description = "")
public class BDynamicReactions {

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long dynamicId;

    private Long userId;

    @Schema(description = "0为点赞 1为点踩 2为评论")
    private Byte type;
}
