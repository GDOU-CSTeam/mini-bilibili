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
 * 角色表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_role")
@Schema(name = "Role", description = "角色表")
public class Role {

      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    @Schema(description = "角色权限字符串")
    private String roleKey;

    @Schema(description = "角色状态（0正常 1停用）")
    private String status;

    @Schema(description = "del_flag")
    private Integer delFlag;

    private Long createBy;

    private LocalDateTime createTime;

    private Long updateBy;

    private LocalDateTime updateTime;

    @Schema(description = "备注")
    private String remark;
}
