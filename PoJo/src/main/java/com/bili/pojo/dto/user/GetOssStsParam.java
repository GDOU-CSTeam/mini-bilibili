package com.bili.pojo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "获取OSS图片上传临时授权参数")
public class GetOssStsParam {

    @Schema(description = "上传后缀名")
    @NotBlank(message = "上传后缀名不能为空")
    private String suffix;

    @Schema(description = "上传类型")
    @NotBlank(message = "上传类型不能为空")
    private String type;
}
