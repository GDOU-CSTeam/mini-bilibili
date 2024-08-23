package com.bili.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bili.entity.User;
import com.bili.entity.Video;
import com.bili.entity.VideoSection;
import com.bili.mapper.UserMapper;
import com.bili.mapper.VideoSectionMapper;
import com.bili.service.UserService;
import com.bili.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;

import java.util.List;

@SpringBootTest
public class MySqlTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VideoSectionMapper videoSectionMapper;


    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        System.out.println(users);
    }

    @Test
    public void testMP(){
        int i = 2;
        //User user = userMapper.selectById(2);
        //userMapper.deleteById(1);
        //User user = loginService.query().eq("email", "23243543").one();
        VideoSection videoSection = videoSectionMapper.selectById(2);
        System.out.println(videoSection);
    }
}
