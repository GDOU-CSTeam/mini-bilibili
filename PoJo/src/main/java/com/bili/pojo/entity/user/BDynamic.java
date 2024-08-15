package com.bili.pojo.entity.user;

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
 * @since 2024-08-07 09:39:55
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_dynamic")
@Schema(name = "BDynamic", description = "")
public class BDynamic {

    @Schema(description = "动态id")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    private Long userId;

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

    @Schema(description = "逻辑删除标记")
    private Byte deleted;
}
