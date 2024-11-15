package com.bili.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParams {
    // 默认起始页码
    public static final long DEFAULT_PAGE_CURRENT = 1L;
    // 默认每页记录数
    public static final long DEFAULT_PAGE_SIZE = 10L;

    // 当前页码
    @Schema(description = "当前页码", example = "1")
    private Long pageNo = DEFAULT_PAGE_CURRENT;

    // 当前每页记录数
    @Schema(description = "每页记录数", example = "2")
    private Long pageSize = DEFAULT_PAGE_SIZE;
}
