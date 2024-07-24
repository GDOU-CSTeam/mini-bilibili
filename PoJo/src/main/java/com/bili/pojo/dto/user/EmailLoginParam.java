package com.bili.pojo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Schema(name = "EmailLoginParam", description = "邮箱登录参数")
public class EmailLoginParam {
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    @Schema(description = "密码")
    @Length(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;
}
