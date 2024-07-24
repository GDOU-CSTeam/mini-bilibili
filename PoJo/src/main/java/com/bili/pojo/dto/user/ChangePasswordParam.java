package com.bili.pojo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(description = "修改密码参数")
public class ChangePasswordParam {
    @Schema(description = "用户名")
    @Length(min = 1, max = 20, message = "用户名长度必须在1-20之间")
    private String username;
    @Schema(description = "旧密码")
    @Length(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;
    @Schema(description = "新密码")
    @Length(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String newPassword;
}
