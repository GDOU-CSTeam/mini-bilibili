package com.bili.pojo.entity;

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
@TableName("b_im_message")
@Schema(name = "BImMessage", description = "")
public class BImMessage {

    @Schema(description = "主键")
      private Long id;

    @Schema(description = "发送用户id")
    private Long toUserId;

    @Schema(description = "接收用户id")
    private Long fromUserId;

    @Schema(description = "发送用户头像")
    private String toUserAvatar;

    @Schema(description = "发送内容")
    private String content;

    @Schema(description = "发送时间")
    private LocalDateTime createTime;

    @Schema(description = "ip地址")
    private String ipSource;

    @Schema(description = "发送用户ip")
    private String ip;

    @Schema(description = "消息是否撤回 0：未撤回  1：撤回")
    private Integer isWithdraw;

    @Schema(description = "是否已读")
    private Integer isRead;

    @Schema(description = "消息类型 1普通消息 2图片")
    private Integer type;

    private Integer code;

    @Schema(description = "视频id")
    private Integer videoId;

    @Schema(description = "通知类型 0系统通知 1：评论 2：关注 3点赞 4收藏 5私信")
    private Integer noticeType;

    @Schema(description = "评论标记 1回复评论 2发表评论")
    private Integer commentMark;

    @Schema(description = "@用户id")
    private Long atUserId;
}
