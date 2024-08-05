package com.bili.common.filter.user;

import com.bili.common.utils.RedisCache;
import com.bili.pojo.constant.user.WebRedisConstants;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.bili.common.utils.JwtUtil;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

// 定义一个组件，用于认证过滤
@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    // 注入JwtToken工具类，用于解析JWT令牌
    @Resource
    JwtUtil jwtToken;
    // 注入RedisUtil工具类，用于访问Redis数据库
    @Resource
    RedisCache redisCache;

    /**
     * 对请求进行过滤，实现认证过程
     * @param request HTTP请求
     * @param response HTTP响应
     * @param chain 过滤器链
     * @throws IOException 如果发生I/O错误
     * @throws ServletException 如果发生Servlet相关错误
     */
    @SneakyThrows
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        // 从请求中获取认证信息
        final UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        // 如果认证信息不为空，则设置到SecurityContextHolder中
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // 继续执行过滤器链
        chain.doFilter(request, response);
    }

    /**
     * 从请求中获取认证信息
     * @param request HTTP请求
     * @return 认证令牌，如果不存在则返回null
     * @throws ParseException 如果解析JWT令牌出错
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws Exception {
        // 从请求头中获取Authorization信息
        String token = request.getHeader("Authorization");
        // 如果没有Authorization信息，则返回null
        if (token == null) {
            return null;
        }
        String userId = null;
        try {
            // 使用JwtToken工具类解析JWT令牌，获取用户ID
            userId = (String) jwtToken.getSubject(token);
        } catch (Exception e) {
            // 解析失败，返回null
            return null;
        }
        // 检查用户是否被拉黑
        final Long dateBlankTime =
                redisCache.getCacheObject(WebRedisConstants.USER_LOGIN_BLACKLIST_KEY + userId);
        if (dateBlankTime != null) {
            final long dateSignTime = jwtToken.getSignTime(token);
            // 如果用户被拉黑，比较签名时间和拉黑时间，如果签名时间在拉黑时间之前，则返回null
            if (dateSignTime <= dateBlankTime) {
                return null;
            }
        }
        // 创建并返回一个UsernamePasswordAuthenticationToken对象，用于认证
        return new UsernamePasswordAuthenticationToken(userId, null, null);
    }
}

