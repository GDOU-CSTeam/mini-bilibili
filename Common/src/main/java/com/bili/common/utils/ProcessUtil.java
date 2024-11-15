package com.bili.common.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class ProcessUtil {

    public static void process(String... command) throws Exception {
        process((File) null, command);
    }

    public static void process(List<String> commandList) throws Exception {
        process((File) null, (String[]) commandList.toArray(new String[0]));
    }

    public static void process(File base, String... command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(new String[0]);
        if (base != null) {
            processBuilder.directory(base);
        }
        processBuilder.command(command);
        processBuilder.start();
    }

    public static String inputToStr(InputStream inputStream) throws UnsupportedEncodingException {
        ByteArrayOutputStream outputSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        try {
            int len;
            while((len = inputStream.read(buffer)) != -1) {
                outputSteam.write(buffer, 0, len);
            }
            outputSteam.close();
            inputStream.close();
        } catch (IOException var5) {
            var5.printStackTrace();
        }
        return outputSteam.toString(StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        Process process = null;
        BufferedReader reader = null;
        try {
            // 执行命令
            process = Runtime.getRuntime().exec("javac SortGvf.java");

            // 读取标准输出
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("STDOUT: " + line);
            }

            // 读取错误输出
            reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = reader.readLine()) != null) {
                System.out.println("STDERR: " + line);
            }

            // 等待进程结束并获取退出码
            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (process != null && process.isAlive()) {
                process.destroy();
            }
        }
    }
}
