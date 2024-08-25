package com.bili.pojo.entity;

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
 * @since 2024-08-25 02:55:13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_exception_log")
@Schema(name = "BExceptionLog", description = "")
public class BExceptionLog {

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "IP")
    private String ip;

    @Schema(description = "ip来源")
    private String ipSource;

    @Schema(description = "请求方法")
    private String method;

    @Schema(description = "描述")
    private String operation;

    @Schema(description = "参数")
    private String params;

    @Schema(description = "异常对象json格式")
    private String exceptionJson;

    @Schema(description = "异常简单信息,等同于e.getMessage")
    private String exceptionMessage;

    @Schema(description = "发生时间")
    private LocalDateTime createTime;
}
