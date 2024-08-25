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
 * 日志表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_user_log")
@Schema(name = "BUserLog", description = "日志表")
public class BUserLog {

    @Schema(description = "主键ID")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "操作用户ID")
    private Long userId;

    @Schema(description = "ip地址")
    private String ip;

    @Schema(description = "操作地址")
    private String address;

    @Schema(description = "操作类型")
    private String type;

    @Schema(description = "操作日志")
    private String description;

    @Schema(description = "操作模块")
    private String model;

    @Schema(description = "操作时间")
    private LocalDateTime createTime;

    @Schema(description = "操作结果")
    private String result;

    @Schema(description = "操作系统")
    private String accessOs;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "客户端类型")
    private String clientType;
}
