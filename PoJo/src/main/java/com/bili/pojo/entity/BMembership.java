package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * B站会员商品表
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:13
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_membership")
@Schema(name = "BMembership", description = "B站会员商品表")
public class BMembership {

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Byte id;

    @Schema(description = "会员类型，“0大会员”，“1超级大会员”")
    private Byte membershipType;

    @Schema(description = "订阅类型，0连续包年，1连续包季，2连续包月，3年度，4季度,月度")
    private Byte subscriptionType;

    @Schema(description = "价格，单位为分")
    private Long price;

    @Schema(description = "折扣率，例如0.66表示6.6折")
    private BigDecimal discount;

    @Schema(description = "有效期，单位为月")
    private Byte durationMonths;

    @Schema(description = "会员权益介绍，JSON格式")
    private String features;

    @Schema(description = "介绍，描述该订阅选项的特点")
    private String description;

    @Schema(description = "状态，1：上架；0：下架")
    private Byte status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
