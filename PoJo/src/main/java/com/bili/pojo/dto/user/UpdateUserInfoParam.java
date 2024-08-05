package com.bili.pojo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "UpdateUserInfoParam", description = "更新用户信息参数")
@Data
public class UpdateUserInfoParam {
    @Schema(description = "用户昵称")
    private String nickname;
    @Schema(description = "用户头像")
    private String avatar;
    @Schema(description = "用户简介")
    private String intro;
    @Schema(description = "用户性别  null为保密 0为男 1为女")
    private Byte sex;
    @Schema(description = "个人中心背景图")
    private String bjCover;
}
