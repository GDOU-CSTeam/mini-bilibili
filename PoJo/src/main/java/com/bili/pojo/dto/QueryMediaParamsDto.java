package com.bili.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * @author Mr.M
 * @version 1.0
 * @description 媒资文件查询请求模型类
 * @date 2022/9/10 8:53
 */
@Data
@ToString
public class QueryMediaParamsDto {

    @Schema(description = "媒资文件名称")
    private String fileName;

    @Schema(description = "媒资类型")
    private String fileType;

    @Schema(description = "审核状态")
    private String auditStatus;
}
