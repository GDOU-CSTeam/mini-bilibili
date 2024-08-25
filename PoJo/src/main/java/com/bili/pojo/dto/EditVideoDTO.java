package com.bili.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class EditVideoDTO {

    /**
     * 视频id
     */
    private Integer id;

    /**
     * 视频链接
     */
    private String videoPath;

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
    private Integer type;

    /**
     * 视频所属的分区
     */
    private Integer sectionId;

    /**
     * 视频标签
     */
    private List<String> tags;

    /**
     * 视频时长（秒）
     */
    private Integer duration;

    /**
     * 视频简介
     */
    private String description;
}
