package com.bili.web;

import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import io.minio.errors.MinioException;
import okio.BufferedSource;
import okio.Okio;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Test;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @description 测试MinIO
 * @author Mr.M
 * @date 2022/9/11 21:24
 * @version 1.0
 */
public class MinioTest {

    static MinioClient minioClient =
            MinioClient.builder()
                    .endpoint("http://159.75.174.133:9000")
                    .credentials("root", "chuangshu")
                    .build();

   //上传文件
    @Test
    public  void upload() {
        try {
            UploadObjectArgs testbucket = UploadObjectArgs.builder()
                    .bucket("testbucket")
//                    .object("test001.mp4")
                    .object("001/test001.mp4")//添加子目录
                    .filename("D:\\WeChat Files\\wxid_usgzeneeygg922\\FileStorage\\Video\\2024-11\\2.mp4")
                    .contentType("video/mp4")//默认根据扩展名确定文件内容类型，也可以指定
                    .build();
            minioClient.uploadObject(testbucket);
            System.out.println("上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("上传失败");
        }
    }

    @Test
    public void delete(){
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket("testbucket").object("001/test001.mp4").build());
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除失败");
        }
    }

    //查询文件
    @Test
    public void getFile() {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket("testbucket").object("1.mp4").build();
        try (
                InputStream inputStream = minioClient.getObject(getObjectArgs);
                FileOutputStream outputStream = new FileOutputStream(new File("D:\\WeChat Files\\wxid_usgzeneeygg922\\FileStorage\\Video\\2024-11\\1-2.mp4"))
        ) {
            // 继续处理
            IOUtils.copy(inputStream, outputStream);
            System.out.println("获取成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMp5() {
        try {
            FileInputStream inputStream = new FileInputStream(new File("E://Download//bigfile_test//merge01.mp4"));
            String md5 = DigestUtils.md5DigestAsHex(inputStream);
            System.out.println(md5);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}