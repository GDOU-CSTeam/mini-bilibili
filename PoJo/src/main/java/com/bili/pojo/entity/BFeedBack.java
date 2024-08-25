package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_feed_back")
@Schema(name = "BFeedBack", description = "")
public class BFeedBack {

    @Schema(description = "ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "详细内容")
    private String content;

    @Schema(description = "图片地址")
    private String imgUrl;

    @Schema(description = "添加时间")
    private LocalDateTime createTime;

    @Schema(description = "反馈类型 1:需求 2：缺陷")
    private Integer type;

    @Schema(description = "状态 0未解决 1解决")
    private Integer status;
}
