package com.bili.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "动态展示对象")
public class DynamicShowVo {
    @Schema(description = "动态id")
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "动态标题")
    private String title;

    @Schema(description = "动态内容")
    private String content;

    @Schema(description = "动态图片")
    private String images;

    @Schema(description = "动态附带视频")
    private Long videoId;

    @Schema(description = "点赞数量")
    private Integer likesCount;

    @Schema(description = "点踩数量")
    private Integer unlikesCount;

    @Schema(description = "评论数量")
    private Integer commentCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
