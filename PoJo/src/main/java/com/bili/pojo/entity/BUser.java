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
 * 系统管理-用户基础信息表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_user")
@Schema(name = "BUser", description = "系统管理-用户基础信息表")
public class BUser {

    @Schema(description = "主键ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "账号")
    private String username;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "登录密码")
    private String password;
}
