package com.bili.pojo.entity.user;

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
 * 用户关注表
 * </p>
 *
 * @author lin
 * @since 2024-07-24 05:19:39
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_concern")
@Schema(name = "BConcern", description = "用户关注表")
public class BConcern {

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "关注的用户id")
    private Long concernUserId;
}
