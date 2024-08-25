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
 * 用户信息表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_user_info")
@Schema(name = "BUserInfo", description = "用户信息表")
public class BUserInfo {

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "硬币数量")
    private Integer coins;

    @Schema(description = "b币数量")
    private Integer bCoins;

    @Schema(description = "贝壳数量")
    private Integer shells;

    @Schema(description = "用户经验")
    private Integer experience;

    @Schema(description = "个性签名")
    private String intro;

    @Schema(description = "个人中心背景图")
    private String bjCover;

    @Schema(description = "出生日期")
    private LocalDateTime birthday;
}
