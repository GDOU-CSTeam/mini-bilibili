package com.bili.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VideoPageVO implements Serializable {

    private Integer id;

    /**
     * 视频封面
     */
    private String coverImage;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频时长（秒）
     */
    private Integer duration;

    /**
     * 观看次数
     */
    private Integer viewCount;

    /**
     * 上传视频的用户
     */
    private String userNickName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
