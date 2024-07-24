package com.bili.pojo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "LoginParam", description = "登录参数")
public class UserNameLoginParam {
    @Length(min = 1, max = 20, message = "用户名长度必须在1-20之间")
    @Schema(description = "用户名")
    private String username;
    @Length(min = 6, max = 20, message = "密码长度必须在6-20之间")
    @Schema(description = "密码")
    private String password;
}
