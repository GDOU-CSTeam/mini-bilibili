package com.sky.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * This class represents the parameters for selecting a page.
 * It is annotated with @Data from Lombok to generate getters, setters, equals, hashCode and toString methods.
 * It is also annotated with @Schema from the OpenAPI 3 specification to provide metadata for the API documentation.
 */
@Data
@Schema(description = "分页查询参数")
public class PageSelectParam {

    /**
     * Represents the page number.
     * It is annotated with @Schema to provide a description in the API documentation.
     * It is also annotated with @Min to enforce that the page number must be greater than 0.
     */
    @Schema(description = "页码")
    @Min(value = 1, message = "页码必须大于0")
    private int page;

    /**
     * Represents the size of the page.
     * It is annotated with @Schema to provide a description in the API documentation.
     * It is also annotated with @Min to enforce that the page size must be greater than 0.
     */
    @Schema(description = "页大小")
    @Min(value = 1, message = "页大小必须大于0")
    private int pageSize;
}
