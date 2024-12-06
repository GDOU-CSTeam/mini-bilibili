package com.bili.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class ProductUserBitmap {

    // 主键字段映射
    @TableId("product_id")
    private Integer productId;

    // BLOB字段映射
    @TableField("bitmap_data")
    private byte[] bitmapData;
}
