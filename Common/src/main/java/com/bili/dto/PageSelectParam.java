package com.bili.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
@Schema(description = "分页查询参数")
public class PageSelectParam {
    @Schema(description = "页码")
    @Min(value = 1, message = "页码必须大于0")
    private int Page;
    @Schema(description = "页大小")
    @Min(value = 1, message = "页大小必须大于0")
    private int PageSize;
}
