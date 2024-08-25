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
 * @since 2024-08-25 02:55:14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("b_voucher")
@Schema(name = "BVoucher", description = "")
public class BVoucher {

    @Schema(description = "主键")
      @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "商铺id")
    private Long shopId;

    @Schema(description = "代金券标题")
    private String title;

    @Schema(description = "副标题")
    private String subTitle;

    @Schema(description = "使用规则")
    private String rules;

    @Schema(description = "支付金额，单位是分。例如200代表2元")
    private Long payValue;

    @Schema(description = "抵扣金额，单位是分。例如200代表2元")
    private Long actualValue;

    @Schema(description = "0,普通券；1,秒杀券")
    private Byte type;

    @Schema(description = "1,上架; 2,下架; 3,过期")
    private Byte status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
