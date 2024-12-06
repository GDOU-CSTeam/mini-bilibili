package com.bili.web.mapper;

import com.bili.pojo.entity.ProductUserBitmap;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

@Mapper
public interface ProductUserMapper {

    // 保存 RoaringBitmap 到数据库
    @Insert("INSERT INTO product_user_bitmap (product_id, bitmap_data)" +
            "VALUES (#{productId}, #{bitmapData, jdbcType=BLOB})")
    void saveRoaringBitmap(ProductUserBitmap bitmap);

    // 从数据库获取 RoaringBitmap
    @Select("SELECT * FROM product_user_bitmap WHERE product_id = #{productId}")
    @Results({
            @Result(column = "bitmap_data", property = "bitmapData", jdbcType = JdbcType.BLOB, typeHandler = org.apache.ibatis.type.BlobTypeHandler.class)
    })
    ProductUserBitmap getRoaringBitmap(@Param("productId") int productId);
}