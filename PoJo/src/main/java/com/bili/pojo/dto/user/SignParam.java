package com.bili.pojo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "SignParam", description = "注册参数")
public class SignParam {
    @Length(min = 1, max = 20, message = "用户名长度必须在1-20之间")
    @Schema(description = "用户名")
    private String username;
    @Length(min = 6, max = 20, message = "密码长度必须在6-20之间")
    @Schema(description = "密码")
    private String password;
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    @Schema(description = "验证码")
    @Length(min = 6, max = 6, message = "验证码长度必须为6")
    private String code;
}
