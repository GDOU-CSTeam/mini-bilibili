package com.bili.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoPageQueryDTO implements Serializable {

    /**
     * 页码
     */
    private Integer page;

    /**
     * 页码大小
     */
    private Integer pageSize;

    /**
     * 标题
     */
    private String title;

    /**
     * 排序方式（0综合排序 1最多播放 2最新发布 3最多弹幕 4最多收藏）
     */
    private Integer order;

    /**
     * 最小时长
     */
    private Integer minDuration;

    /**
     * 最大时长
     */
    private Integer maxDuration;


    /**
     * 分区id
     */
    private Integer sectionId;
}
