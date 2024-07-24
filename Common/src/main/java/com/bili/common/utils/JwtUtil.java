package com.bili.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {

    @Value("${jwt.ttl}")
    //有效期为
    public static Long JWT_TTL;
    @Value("${jwt.key}")
    //设置秘钥明文
    public static String JWT_KEY;

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public String createJWT(String subject) {
            JwtBuilder builder = getJwtBuilder(subject, null);// 设置过期时间
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
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis == null){
            ttlMillis = JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }


    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }
    
    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 从令牌中提取签名时间。
     *
     * @param jwt 待验证的JWT令牌。
     * @return 令牌中携带的签名时间。
     */
    public String getSignTime(String jwt) throws Exception {
        Claims claims = parseJWT(jwt);
        // 获取签名时间
        Object iat = claims.get("iat");
        return iat.toString();
    }
}