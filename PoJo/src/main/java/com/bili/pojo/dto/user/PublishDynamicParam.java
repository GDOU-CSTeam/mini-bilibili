package com.bili.pojo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "发布动态参数")
public class PublishDynamicParam {

    @NotBlank(message = "动态标题不能为空")
    @Schema(description = "动态标题")
    private String title;

    @NotBlank(message = "动态内容不能为空")
    @Schema(description = "动态内容")
    private String content;

    @Schema(description = "动态图片")
    private List<String> images;

    @Schema(description = "动态附带视频ID")
    private String videoId;
}
