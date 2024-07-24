package com.bili.pojo.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2024-07-24 05:19:39
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_user_info")
@Schema(name = "BUserInfo", description = "用户信息表")
public class BUserInfo {

    @Schema(description = "用户ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "硬币数量")
    private Integer coins;

    @Schema(description = "用户经验")
    private Integer experience;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户简介")
    private String intro;

    @Schema(description = "用户性别  null为保密 0为男 1为女")
    private Byte sex;

    @Schema(description = "是否禁用，0:禁用，1:启用")
    private Byte isDisable;

    @Schema(description = "个人中心背景图")
    private String bjCover;
}
