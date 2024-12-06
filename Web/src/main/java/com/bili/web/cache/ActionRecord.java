package com.bili.web.cache;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationException;
import org.apache.ibatis.annotations.SelectKey;
import org.roaringbitmap.RoaringBitmap;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.KeyStore.Entry;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class ActionRecord {

    private Long id; // 例如 用户 ID 或 视频 ID
    private Map<Byte, RoaringBitmap> actionBitmaps; // 操作类型 -> RoaringBitmap

    public ActionRecord(Long id) {
        this.id = id;
        this.actionBitmaps = new HashMap<>();
    }

    // 记录某个操作的访问
    public void addAction(Byte actionType, int dayOffset) {
        actionBitmaps.computeIfAbsent(actionType, k -> new RoaringBitmap()).add(dayOffset); // 在指定位置记录该操作
    }

    // 获取某个操作的访问记录
    public boolean getAction(Byte actionType, int dayOffset) {
        RoaringBitmap bitmap = actionBitmaps.get(actionType);
        if (bitmap != null) {
            return bitmap.contains(dayOffset);
        }
        return false;
    }

    // 取消记录某个操作的访问
    public void delAction(Byte actionType, int dayOffset) {
        RoaringBitmap bitmap = actionBitmaps.get(actionType);
        if (bitmap != null) {
            bitmap.remove(dayOffset);
            if (bitmap.isEmpty()) {
                actionBitmaps.remove(actionType);
            }
        }
    }

    // 序列化操作记录为字节数组
    public Map<Byte, byte[]> serialize() {
        Map<Byte, byte[]> serializedData = new HashMap<>();
        for (Map.Entry<Byte, RoaringBitmap> entry : actionBitmaps.entrySet()) {
            RoaringBitmap bitmap = entry.getValue();
            byte[] array = new byte[bitmap.serializedSizeInBytes()];
            bitmap.serialize(ByteBuffer.wrap(array));
            serializedData.put(entry.getKey(), array);
        }
        return serializedData;
    }

    // 反序列化方法：从字节数组恢复 ActionRecord 对象
    public static ActionRecord fromByteArray(Long id, Map<Byte, byte[]> data) throws IOException {
        ActionRecord record = new ActionRecord(id);
        for (Map.Entry<Byte, byte[]> entry : data.entrySet()) {
            RoaringBitmap bitmap = new RoaringBitmap();
            try {
                bitmap.deserialize(ByteBuffer.wrap(entry.getValue()));
            } catch (IOException e) {
                throw new IOException("将字节数组序列化为RoaringBitmap失败！", e);
            }
            record.actionBitmaps.put(entry.getKey(), bitmap);
        }
        return record;
    }

   public Map.Entry<Byte, RoaringBitmap> addActionBitmap(Byte actionType, byte[] data) {
       RoaringBitmap bitmap = actionBitmaps.get(actionType);
       if (bitmap == null || bitmap.isEmpty()) {
           bitmap = new RoaringBitmap();
           try {
               bitmap.deserialize(ByteBuffer.wrap(data));
               actionBitmaps.put(actionType, bitmap);
           } catch (IOException e) {
               // 记录异常信息
               log.error("反序列化 RoaringBitmap 失败: " + e.getMessage());
               e.printStackTrace();
               // 返回一个空的 Entry
               return new AbstractMap.SimpleEntry<>(actionType, null);
           }
       }
       return new AbstractMap.SimpleEntry<>(actionType, bitmap);
   }

    // 获取id
    public Long getId() {
        return id;
    }

    // 获取单个操作记录的 Bitmap
    public RoaringBitmap getActionBitmap(Byte actionType) {
        return actionBitmaps.get(actionType);
    }

    // 获取操作记录集合
    public Map<Byte, RoaringBitmap> getActionBitmaps() {
        return actionBitmaps;
    }
}