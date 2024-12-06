package com.bili.web.service.impl;

import com.bili.common.utils.RoaringBitmapUtil;
import com.bili.web.mapper.ProductUserMapper;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.sql.SQLException;

@Service
public class ProductUserService {

    //@Autowired
    //private ProductUserMapper productUserMapper;
    //
    //// 保存 RoaringBitmap 到数据库
    //@Transactional
    //public void saveRoaringBitmap(int productId, byte[] bitmapData) throws IOException {
    //    productUserMapper.saveRoaringBitmap(productId, bitmapData);
    //}
    //
    //// 从数据库获取 RoaringBitmap
    //public RoaringBitmap getRoaringBitmap(int productId) throws IOException {
    //    //byte[] bitmapData = productUserMapper.getRoaringBitmap(productId);
    //    //if (bitmapData != null) {
    //    //    return RoaringBitmapUtil.deserialize(bitmapData);
    //    //}
    //    //return null;
    //    return null;
    //}
    //
    //// 获取唯一用户数
    //public int getUniqueUsers(int productId) throws IOException {
    //    RoaringBitmap bitmap = getRoaringBitmap(productId);
    //    if (bitmap != null) {
    //        return bitmap.getCardinality();
    //    }
    //    return 0;  // 没有数据
    //}
}
