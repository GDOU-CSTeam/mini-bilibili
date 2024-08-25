package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 秒杀优惠券表，与优惠券是一对一关系
 * </p>
 *
 * @author lin
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_seckill_voucher")
@Schema(name = "BSeckillVoucher", description = "秒杀优惠券表，与优惠券是一对一关系")
public class BSeckillVoucher {

    @Schema(description = "关联的优惠券的id")
      private Long voucherId;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "生效时间")
    private LocalDateTime beginTime;

    @Schema(description = "失效时间")
    private LocalDateTime endTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
