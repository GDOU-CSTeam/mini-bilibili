package com.bili.test;

import com.bili.pojo.entity.admin.LoginUser;
import com.bili.pojo.entity.admin.TestUser;
import com.bili.common.utils.RedisCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisCache redisCache;

    @Test
    void testString() {
        // 写入一条String数据
        redisTemplate.opsForValue().set("name", "胡歌");
        // 获取string数据
        Object name = redisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }


    // JSON序列化工具
    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testSaveUser() throws JsonProcessingException {
        // 创建对象
        TestUser user = new TestUser("虎哥", 21);
        // 手动序列化
        String json = mapper.writeValueAsString(user);
        // 写入数据
        stringRedisTemplate.opsForValue().set("user:200", json);

        // 获取数据
        String jsonUser = stringRedisTemplate.opsForValue().get("user:200");
        // 手动反序列化
        TestUser user1 = mapper.readValue(jsonUser, TestUser.class);
        System.out.println("user1 = " + user1);
    }

    @Test
    void testUtilSaveUser() {
        // 创建对象
        TestUser user = new TestUser("虎哥", 21);
        redisCache.setCacheObject("user:300",user);
        System.out.println(user);
    }

    @Test
    void testGetObject() throws JsonProcessingException {
        String s = stringRedisTemplate.opsForValue().get("login:1");
        LoginUser loginUser = mapper.readValue(s, LoginUser.class);
        System.out.println(loginUser);
    }

    @Test
    void testUtilGetObject() throws JsonProcessingException {
        LoginUser loginUser = redisCache.getCacheObject("login:1");
        System.out.println(loginUser);
    }

}
