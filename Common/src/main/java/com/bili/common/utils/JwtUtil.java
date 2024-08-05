package com.bili.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {

    //有效期为
    @Value("${jwt.ttl}")
    public Long jwtTtl;
    //设置秘钥明文
    @Value("${jwt.key}")
    public String jwtKey;


    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject,null);// 设置过期时间
        return builder.compact();
    }
 
    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis);// 设置过期时间
        return builder.compact();
    }
 
    private JwtBuilder getJwtBuilder(String subject, Long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        HashMap<String, Object> map = new HashMap<>();
        map.put("subject", subject);
        map.put("signTime", nowMillis);
        if (ttlMillis == null) {
            ttlMillis = jwtTtl;
        }
        long expMillis = nowMillis + ttlMillis * 1000;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setClaims(map)     // 签发时间
                .signWith(signatureAlgorithm, jwtKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    
    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws Exception {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    public Object getSubject(String jwt) throws Exception {
        Claims claims = parseJWT(jwt);
        // 获取用户信息
        return claims.get("subject");
    }

    /**
     * 从令牌中提取签名时间。
     *
     * @param jwt 待验证的JWT令牌。
     * @return 令牌中携带的签名时间。
     */
    public Long getSignTime(String jwt) throws Exception {
        Claims claims = parseJWT(jwt);
        // 获取签名时间
        Object iat = claims.get("signTime");
        return (Long) iat;
    }
}