package com.bili.pojo.entity;

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
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_voucher_order")
@Schema(name = "BVoucherOrder", description = "")
public class BVoucherOrder {

    @Schema(description = "主键")
      private Long id;

    @Schema(description = "下单的用户id")
    private Long userId;

    @Schema(description = "购买的代金券id")
    private Long voucherId;

    @Schema(description = "支付方式 1：余额支付；2：支付宝；3：微信")
    private Byte payType;

    @Schema(description = "订单状态，1：未支付；2：已支付；3：已核销；4：已取消；5：退款中；6：已退款")
    private Byte status;

    @Schema(description = "下单时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "核销时间")
    private LocalDateTime useTime;

    @Schema(description = "退款时间")
    private LocalDateTime refundTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
