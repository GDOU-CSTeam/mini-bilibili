package com.bili.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EditUserInfoDTO {

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "用户签名")
    private String intro;

    @Schema(description = "出生日期")
    private LocalDateTime birthday;

    @Schema(description = "个人中心背景图")
    private String bjCover;
}
