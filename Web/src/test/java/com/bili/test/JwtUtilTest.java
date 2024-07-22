package com.bili.test;

import com.bili.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JwtUtilTest {


    @Test
    public void testJwtPa() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIzYWQxZjE0NjRiYTU0NzhkOTU5YTQyYzRiNmQ3MTE3MyIsInN1YiI6IjEiLCJpc3MiOiJiaWxpIiwiaWF0IjoxNzIxNDg4NjkzLCJleHAiOjE3MjE1NzUwOTN9.H8yNBXPGAx7E2cBlKxcAsceuIju981VlC7jPn_zL8pE";
        Claims claims = JwtUtil.parseJWT(jwt);
        System.out.println(claims); //claims = {jti=3ad1f1464ba5478d959a42c4b6d71173, sub=1, iss=bili, iat=1721488693, exp=1721575093}
        // 获取签名时间
        // 直接转换为long类型
        Integer iat = (Integer) claims.get("iat");
        System.out.println(iat);
        //Object iat = claims.get("iat");
        //System.out.println(iat.toString());
        // 直接获取Date类型
        //Object iat = claims.get("iat");
        //System.out.println(iat);

        //Date iatDate = claims.get("iat", Date.class);
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String iatStr = sdf.format(iatDate);
        //System.out.println("iat: " + iatStr);
    }



}
