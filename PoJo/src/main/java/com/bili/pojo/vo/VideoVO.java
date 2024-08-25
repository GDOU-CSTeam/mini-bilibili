package com.bili.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName(value = "videos")
public class VideoVO implements Serializable {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 视频类型（0自制 1转载）
     */
    private String type;

    /**
     * 视频所属的分区
     */
    private String section;

    /**
     * 视频简介
     */
    private String description;

    /**
     * 视频时长（秒）
     */
    private Integer duration;

    /**
     * 观看次数
     */
    private Integer viewCount;

    /**
     * 视频的状态（0待审核、1已发布、2被限流、3已删除）
     */
    private String status;

    /**
     * 视频路径
     */
    private String videoPath;

    /**
     * 视频标签
     */
    private List<String> tags;

    /**
     * 上传视频的用户
     */
    private Integer userId;

    /**
     * 点赞数
     */
    private String likes;

    /**
     * 投币数
     */
    private String coins;

    /**
     * 收藏数
     */
    private String collections;

    /**
     * 转发数
     */
    private String shares;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
