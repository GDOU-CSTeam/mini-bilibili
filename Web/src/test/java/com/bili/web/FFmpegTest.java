package com.bili.web;

import jakarta.servlet.ServletOutputStream;
import org.junit.Test;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FFmpegTest {

    @Test
    public void testProcessBuilder() throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("D://develop//Sublime Text//sublime_text.exe");
        //将标准输入流和错误输入流合并，通过标准输入流程读取信息
        builder.redirectErrorStream(true);
        Process process = builder.start();
        // 等待进程结束并获取退出值
        int exitCode = process.waitFor();
        System.out.println("Exited with code: " + exitCode);
        InputStream inputStream = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String temp = null;
        while((temp = br.readLine()) != null){
            System.out.println(new String(temp.getBytes(StandardCharsets.UTF_8)));
        }
    }

    @Test
    public void testProcess() throws Exception {
        Process process = Runtime.getRuntime().exec("javac");
        if(process.waitFor() != 0){
            System.exit(1);
        }
        InputStream inputStream = process.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String temp = null;
        while((temp = br.readLine()) != null){
            System.out.println(new String(temp.getBytes(StandardCharsets.UTF_8)));
        }
    }

    @Test
    public void testRuntime() throws IOException {
        Process process = Runtime.getRuntime().exec("D://develop//Sublime Text//sublime_text.exe");

        long freeMemory = Runtime.getRuntime().freeMemory();
        System.out.println(convertFileSize(freeMemory));


        long totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println(convertFileSize(totalMemory));


        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println(convertFileSize(maxMemory));

        int processors = Runtime.getRuntime().availableProcessors();
        System.out.println(processors);

        Runtime.getRuntime().exit(0);
    }

    @Test
    public void testSystemClass(){
        Byte b = 9;
        System.out.println(b);
    }

    private long convertFileSize(long byteSize){
        byteSize = (long) (byteSize / (Math.pow(1024, 2)));
        return byteSize;
    }
}
