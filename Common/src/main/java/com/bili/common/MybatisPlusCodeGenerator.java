package com.bili.common;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MybatisPlusCodeGenerator {

    private static final String dbUrl = "jdbc:mysql://xx:3306/xx?serverTimezone=UTC&characterEncoding=UTF-8&useUnicode=true&useSSL=false";
    private static final String dbUsername = "root";
    private static final String dbPassword = "";


    public static void main(String[] args) {
        FastAutoGenerator.create(dbUrl, dbUsername, dbPassword)
                // 全局配置
                .globalConfig(builder -> {
                    builder.author("lin") // 设置作者
                            .commentDate("yyyy-MM-dd hh:mm:ss")   //注释日期
                            .outputDir(System.getProperty("user.dir") + "/generation") // 指定输出目录
                            .disableOpenDir() //禁止打开输出目录，默认打开
                            .enableSwagger() //开启 swagger 模式
                            .enableSpringdoc() //开启 springdoc 模式
                    ;
                })
                // 包配置
                .packageConfig(builder -> {
                    builder.parent("com.sky.user") // 设置父包名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/generation/mapper")); // 设置mapperXml生成路径
                })
                // 策略配置
                .strategyConfig(builder -> {
                    try {
                        builder.addInclude(getTables("video")) // 设置需要生成的表名
                                .addTablePrefix("sys_") // 设置过滤表前缀
                                // Entity 策略配置
                                .entityBuilder()
                                .enableLombok() //开启 Lombok
                                .enableFileOverride() // 覆盖已生成文件
                                .enableChainModel() // 开启链式模型
                                .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略：下划线转驼峰命
                                .columnNaming(NamingStrategy.underline_to_camel)//数据库表字段映射到实体的命名策略：下划线转驼峰命
                                .disableSerialVersionUID() //禁用生成 serialVersionUID
                                // Mapper 策略配置
                                .mapperBuilder()
                                .enableFileOverride() // 覆盖已生成文件
                                .enableMapperAnnotation() // 开启 @Mapper 注解
                                // Service 策略配置
                                .serviceBuilder()
                                .enableFileOverride() // 覆盖已生成文件
                                .formatServiceFileName("%sService") //格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                                .formatServiceImplFileName("%sServiceImpl") //格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl
                                // Controller 策略配置
                                .controllerBuilder()
                                .enableFileOverride() // 覆盖已生成文件
                                .enableRestStyle() // 开启 @RestController 注解
                        ;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .execute();
    }

    private static String[] getTables(String dbName) throws Exception {
        List<String> tables = new ArrayList<>();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl,dbUsername,dbPassword);
            String sql = "select table_name from information_schema.tables where table_schema=?";
            ps = connection.prepareStatement(sql);
            ps.setString(1,dbName);
            resultSet = ps.executeQuery();
            while (resultSet.next()) {
                tables.add(resultSet.getString("table_name"));
            }
            return tables.toArray(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        throw new Exception("数据库连接异常！");
    }
}
