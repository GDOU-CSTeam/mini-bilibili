package com.bili.web.controller;


import com.bili.common.utils.Result;
import com.bili.pojo.entity.ProductUserBitmap;
import com.bili.pojo.entity.UserActionRecord;
import com.bili.web.cache.ActionRecord;
import com.bili.web.mapper.UserActionRecordsMapper;
import com.bili.web.service.IUserActionRecordsService;
import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hygl
 * @since 2024-12-06
 */
@RestController
@RequestMapping("/user-action-records")
public class UserActionRecordsController {

    @Autowired
    private LoadingCache<Long, ActionRecord> guavaCache;

    @Autowired
    private IUserActionRecordsService userActionRecordsService;

    @Autowired
    private UserActionRecordsMapper userActionRecordsMapper;

    @GetMapping("/{userId}")
    public Result testCache(@PathVariable Long userId) throws ExecutionException {
        ActionRecord actionRecord = guavaCache.get(userId);
        return Result.success();
    }


    @GetMapping("/add/{userId}")
    public Result testAddCache(@PathVariable Long userId) throws ExecutionException {
        // 创建一个 RoaringBitmap
        RoaringBitmap bitmap = new RoaringBitmap();

        // 向 RoaringBitmap 中添加 1 万个值
        for (int i = 1; i <= 10000; i++) {
            bitmap.add(i);
        }
        // 序列化 RoaringBitmap
        byte[] array = new byte[bitmap.serializedSizeInBytes()];
        bitmap.serialize(ByteBuffer.wrap(array));

        UserActionRecord userActionRecord = new UserActionRecord();
        userActionRecord.setUserId(userId);
        userActionRecord.setActionType((byte) 1);
        userActionRecord.setActionData(array);
        userActionRecordsMapper.saveRoaringBitmap(userActionRecord);


        List<UserActionRecord> userRecords = userActionRecordsMapper.getUserRecords(userId);
        for (UserActionRecord userRecord : userRecords) {
            RoaringBitmap deserialize = deserialize(userRecord.getActionData());
            System.out.println(deserialize);
        }
        return Result.success();
    }

    public RoaringBitmap deserialize(byte[] data) {
        // 反序列化 RoaringBitmap
        RoaringBitmap bitmap = new RoaringBitmap();
        try {
            bitmap.deserialize(ByteBuffer.wrap(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }


}
