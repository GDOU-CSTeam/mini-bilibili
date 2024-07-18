package com.sky.common.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtToken {

    @Value("${jwt.key}")
    private String KEY;
    public String createAccessToken(String userid){
        HashMap<String, Object> headers = new HashMap<>();
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.HOUR, 24);

        return JWT.create()
                // 第一部分Header
                .withHeader(headers)
                // 第二部分Payload
                .withClaim("userid", userid)
                .withClaim("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .withExpiresAt(expires.getTime())
                // 第三部分Signature
                .sign(Algorithm.HMAC256(String.valueOf(KEY)));
    }

    public String createRefreshToken(String userid){
        HashMap<String, Object> headers = new HashMap<>();
        Calendar expires = Calendar.getInstance();
        expires.add(Calendar.HOUR, 24*7);

        return JWT.create()
                // 第一部分Header
                .withHeader(headers)
                // 第二部分Payload
                .withClaim("userid", userid)
                .withClaim("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                .withExpiresAt(expires.getTime())
                // 第三部分Signature
                .sign(Algorithm.HMAC256(String.valueOf(KEY)));
    }


    public String get(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(KEY)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        return claims.get("userid").asString();
    }

    public String getSignTime(String token){
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(KEY)).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        Map<String, Claim> claims = decodedJWT.getClaims();
        return claims.get("time").asString();
    }
}