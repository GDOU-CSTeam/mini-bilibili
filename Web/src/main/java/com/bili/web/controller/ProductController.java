package com.bili.web.controller;

import com.bili.web.service.impl.ProductUserService;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/product")
public class ProductController {

    //@Autowired
    //private ProductUserService productUserService;
    //
    //// 保存商品的唯一用户 RoaringBitmap
    //@PostMapping("/{productId}/saveBitmap")
    //public String saveBitmap(@PathVariable int productId, @RequestBody RoaringBitmap bitmap) throws IOException {
    //    //productUserService.saveRoaringBitmap(productId, bitmap);
    //    return "Saved successfully!";
    //}
    //
    //// 获取商品的唯一用户 RoaringBitmap
    //@GetMapping("/{productId}/getBitmap")
    //public int getUniqueUsers(@PathVariable int productId) throws IOException {
    //    return productUserService.getUniqueUsers(productId);
    //}
}
