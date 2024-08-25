package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 说说表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_say")
@Schema(name = "BSay", description = "说说表")
public class BSay {

    @Schema(description = "主键id")
      private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "图片地址 逗号分隔  最多九张")
    private String imgUrl;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "发表地址。可输入或者地图插件选择")
    private String address;

    @Schema(description = "是否开放查看  0未开放 1开放")
    private Integer isPublic;

    @Schema(description = "发表时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
