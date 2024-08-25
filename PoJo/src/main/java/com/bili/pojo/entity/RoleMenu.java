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
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role_menu")
@Schema(name = "RoleMenu", description = "")
public class RoleMenu {

    @Schema(description = "角色ID")
      @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

    @Schema(description = "菜单id")
      private Long menuId;
}
