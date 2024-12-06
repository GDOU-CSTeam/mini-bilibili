package com.bili.common.utils;

import org.roaringbitmap.RoaringBitmap;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RoaringBitmapUtil {

    // 序列化 RoaringBitmap 为字节数组
    public static byte[] serialize(RoaringBitmap bitmap) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        bitmap.serialize(dataOutputStream);
        dataOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    // 从字节数组反序列化为 RoaringBitmap
    public static RoaringBitmap deserialize(byte[] data) throws IOException {
        RoaringBitmap bitmap = new RoaringBitmap();
        try (DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(data))) {
            bitmap.deserialize(dataInputStream);
        }
        return bitmap;
    }
}
