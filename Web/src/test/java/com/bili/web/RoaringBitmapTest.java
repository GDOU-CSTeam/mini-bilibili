package com.bili.web;
import com.bili.common.utils.RoaringBitmapUtil;
import com.bili.pojo.entity.ProductUserBitmap;
import com.bili.web.mapper.ProductUserMapper;
import com.bili.web.mapper.UserActionRecordsMapper;
import com.bili.web.service.impl.ProductUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) // 使用 JUnit 5 扩展
@SpringBootTest
public class RoaringBitmapTest {

    @Autowired
    private ProductUserMapper productUserMapper;

    @Autowired
    private UserActionRecordsMapper userActionRecordsMapper;

    private RoaringBitmap bitmap;

    @BeforeEach
    public void setUp() {
        // 创建一个 RoaringBitmap
        bitmap = new RoaringBitmap();

        // 向 RoaringBitmap 中添加 1 万个值
        for (int i = 1; i <= 10000; i++) {
            bitmap.add(i);
        }

        // 打印出 bitmap 的一些基本信息
        System.out.println("RoaringBitmap 生成完毕！");
        System.out.println("总共的唯一值数量：" + bitmap.getCardinality()); // 获取唯一值的数量
        System.out.println("RoaringBitmap 是否包含 5000： " + bitmap.contains(5000)); // 检查某个值是否存在
    }

    @Test
    public void testGetUniqueUsers0() throws IOException {
        int productId = 222; // 商品ID
        // 获取 RoaringBitmap 在内存中的大小
        //通常来说，sizeInBytes() 会比 serializedSizeInBytes() 大，因为 RoaringBitmap 内部会有一些额外的内存结构来支持高效的操作。
        //8202- >8208 差不多 1w
        System.out.println(bitmap.getSizeInBytes() + "- >" + bitmap.serializedSizeInBytes());
        // 序列化 RoaringBitmap
        byte[] array = new byte[bitmap.serializedSizeInBytes()];
        bitmap.serialize(ByteBuffer.wrap(array));


        ProductUserBitmap productUserBitmap = new ProductUserBitmap();
        productUserBitmap.setProductId(productId);
        productUserBitmap.setBitmapData(array);
        productUserMapper.saveRoaringBitmap(productUserBitmap);


        // 反序列化 RoaringBitmap
        RoaringBitmap integers = new RoaringBitmap();

        ProductUserBitmap array2 = productUserMapper.getRoaringBitmap(productId);
        integers.deserialize(ByteBuffer.wrap(array2.getBitmapData()));

        // 打印结果验证
        System.out.println("执行完成");
        System.out.println("原始 bitmap 唯一值数量：" + bitmap.getCardinality());
        System.out.println("反序列化后的 bitmap 唯一值数量：" + integers.getCardinality());

        // 验证反序列化后的 bitmap 是否包含原始 bitmap 中的某些元素
        for (int i = 10000; i <= 10010; i++) { // 检查一些元素
            if (integers.contains(i)) {
                System.out.println("反序列化后的 bitmap 包含元素: " + i);
            } else {
                System.out.println("反序列化后的 bitmap 不包含元素: " + i);
            }
        }
    }


    @Test
    public void testGetUniqueUsers() throws IOException {
        int productId = 222; // 商品ID
        // 获取 RoaringBitmap 在内存中的大小
        //通常来说，sizeInBytes() 会比 serializedSizeInBytes() 大，因为 RoaringBitmap 内部会有一些额外的内存结构来支持高效的操作。
        //8202- >8208 差不多 1w
        System.out.println(bitmap.getSizeInBytes() + "- >" + bitmap.serializedSizeInBytes());
        // 序列化 RoaringBitmap
        byte[] array = new byte[bitmap.serializedSizeInBytes()];
        bitmap.serialize(ByteBuffer.wrap(array));

        ProductUserBitmap productUserBitmap = new ProductUserBitmap();
        productUserBitmap.setProductId(productId);
        productUserBitmap.setBitmapData(array);
        productUserMapper.saveRoaringBitmap(productUserBitmap);


        // 反序列化 RoaringBitmap
        RoaringBitmap integers = new RoaringBitmap();

        ProductUserBitmap array2 = productUserMapper.getRoaringBitmap(productId);
        integers.deserialize(ByteBuffer.wrap(array2.getBitmapData()));

        // 打印结果验证
        System.out.println("执行完成");
        System.out.println("原始 bitmap 唯一值数量：" + bitmap.getCardinality());
        System.out.println("反序列化后的 bitmap 唯一值数量：" + integers.getCardinality());

        // 验证反序列化后的 bitmap 是否包含原始 bitmap 中的某些元素
        for (int i = 10000; i <= 10010; i++) { // 检查一些元素
            if (integers.contains(i)) {
                System.out.println("反序列化后的 bitmap 包含元素: " + i);
            } else {
                System.out.println("反序列化后的 bitmap 不包含元素: " + i);
            }
        }
    }

    @Test
    public void testGetUniqueUsers2() throws IOException {
        RoaringBitmap integers = new RoaringBitmap();
        ProductUserBitmap array2 = productUserMapper.getRoaringBitmap(333);
        integers.deserialize(ByteBuffer.wrap(array2.getBitmapData()));
    }

    @Test
    public void testGetUniqueUsers4() throws IOException {
        ProductUserBitmap productUserBitmap = new ProductUserBitmap();
        productUserBitmap.setProductId(222);
        productUserBitmap.setBitmapData(new byte[6]);
        productUserMapper.saveRoaringBitmap(productUserBitmap);
    }

    @Test
    public void testGetUniqueUsers3() throws IOException {
        String jdbcUrl = "jdbc:mysql://159.75.174.133:3306/bililike";
        String username = "root";
        String password = "chuangshu";

        // 查询语句
        String sql = "SELECT bitmap_data FROM product_user_bitmap WHERE product_id = ?";

        // 使用Connection来执行查询并获取BLOB
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 设置查询参数
            stmt.setInt(1, 123); // 假设我们查询的product_id为123

            // 执行查询
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // 获取BLOB字段
                    Blob blob = rs.getBlob("bitmap_data");

                    // 将BLOB转换为byte数组
                    byte[] bitmapData = blob.getBytes(1, (int) blob.length());

                    // 处理byte[]，例如保存到文件、处理数据等
                    System.out.println("Retrieved " + bitmapData.length + " bytes of BLOB data.");

                    // 你可以进一步处理bitmapData（例如保存为文件、使用等）
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}