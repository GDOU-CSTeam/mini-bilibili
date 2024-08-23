package com.bili.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 欢迎光临
 * @since 2024-08-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("b_follow")
@Schema(name="用户关注对象", description="")
public class Follow implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "用户id")
    private Long userId;

    @Schema(name = "关联的用户id")
    private Long followUserId;

    @Schema(name = "创建时间")
    private LocalDateTime createTime;


}
