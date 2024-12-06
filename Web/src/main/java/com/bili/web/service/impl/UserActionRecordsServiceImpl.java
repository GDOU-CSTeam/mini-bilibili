package com.bili.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bili.pojo.entity.UserActionRecord;
import com.bili.web.cache.ActionRecord;
import com.bili.web.mapper.UserActionRecordsMapper;
import com.bili.web.service.IUserActionRecordsService;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hygl
 * @since 2024-12-06
 */
@Service
public class UserActionRecordsServiceImpl extends ServiceImpl<UserActionRecordsMapper, UserActionRecord> implements IUserActionRecordsService {

    @Autowired
    private UserActionRecordsMapper userActionRecordsMapper;

    @Override
    public void savaToDataBase(ActionRecord actionRecord) {
        // 获取用户id
        Long userId = actionRecord.getId();
        // 遍历序列化存入数据库
        Map<Byte, RoaringBitmap> actionBitmaps = actionRecord.getActionBitmaps();
        actionBitmaps.forEach((key, value) -> {
            // 查询是否存在
            LambdaQueryWrapper<UserActionRecord> wrapper = new LambdaQueryWrapper<UserActionRecord>()
                    .eq(UserActionRecord::getUserId, userId)
                    .eq(UserActionRecord::getActionType, key);
            long count = count(wrapper);

            if (count > 0) {
                // 更新数据
                LambdaUpdateWrapper<UserActionRecord> updateWrapper = new LambdaUpdateWrapper<UserActionRecord>()
                        .eq(UserActionRecord::getUserId, userId)
                        .eq(UserActionRecord::getActionType, key)
                        .set(UserActionRecord::getActionData, serialize(value));
                update(updateWrapper);
            } else {
                // 新增数据
                UserActionRecord record = new UserActionRecord();
                record.setId(userId);
                record.setActionType(key);
                record.setActionData(serialize(value));
                save(record);
            }
        });
    }

    @Override
    public ActionRecord getFormDataBase(Long userId) {
        List<UserActionRecord> list = userActionRecordsMapper.getUserRecords(userId); //查不出来

        List<UserActionRecord> userRecords = userActionRecordsMapper.getUserRecords(userId); // 查的出来

        System.out.println("一切正常");

        if (list.isEmpty()) {
            // 没有数据
            return new ActionRecord(userId);
        }
        // 解析数据
        Map<Byte, RoaringBitmap> map = new HashMap<>();
        for (UserActionRecord userActionRecord : list) {
            RoaringBitmap bitmap = deserialize(userActionRecord.getActionData());
            map.put(userActionRecord.getActionType(), bitmap);
         }
        // 返回数据
        ActionRecord actionRecord = new ActionRecord(userId);
        actionRecord.setActionBitmaps(map);
        return actionRecord;
    }


    // 序列化操作记录为字节数组
    public byte[] serialize(RoaringBitmap bitmap) {
        byte[] array = new byte[bitmap.serializedSizeInBytes()];
        bitmap.serialize(ByteBuffer.wrap(array));
        return array;
    }

    public RoaringBitmap deserialize(byte[] data) {
        // 反序列化 RoaringBitmap
        RoaringBitmap bitmap = new RoaringBitmap();
        try {
            bitmap.deserialize(ByteBuffer.wrap(data));
        } catch (IOException e) {
            log.error("反序列化 RoaringBitmap出错");
            e.printStackTrace();
        }
        return bitmap;
    }
}
