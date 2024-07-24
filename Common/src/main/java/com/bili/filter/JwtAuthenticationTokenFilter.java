package com.bili.filter;

import com.bili.constant.RedisConstants;
import com.bili.entity.LoginUser;
import com.bili.utils.JwtUtil;
import com.bili.utils.RedisCache;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    public RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            //不携带token放行
            filterChain.doFilter(request,response);
            return;
        }
        //解析token
        String userId;
        Integer signTime;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "login:"+ userId;
        LoginUser loginUser = redisCache.getCacheObject(redisKey);
        //刷新token有效期
        redisCache.expire(redisKey, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        if(Objects.isNull(loginUser)){
            throw new RuntimeException("用户未登录");
        }

        //存入SecurityContextHolder
        //获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null, loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request,response);
    }
}
