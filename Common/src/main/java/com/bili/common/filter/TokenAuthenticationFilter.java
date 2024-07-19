package com.bili.common.filter;

import com.bili.common.utils.JwtToken;
import com.bili.common.utils.RedisUtil;
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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    JwtToken jwtToken;
    @Resource
    RedisUtil redisUtil;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //logger.info("uri:" + request.getRequestURI());
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) throws ParseException {
        //String authority = "ROLE_admin";
        //List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
        String token = request.getHeader("Authorization");
        //判断是否有token
        if (token == null) {
            return null;
        }
        String userid = null;
        //验证token
        try{
            userid = jwtToken.get(token);
        }catch (Exception e){
            return null;
        }
        //验证是否被拉黑
        String date_sign_string = jwtToken.getSignTime(token);
        String date_blank_string = redisUtil.get("Black:"+userid);
        if (date_blank_string != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date_sign = sdf.parse(date_sign_string);
            Date date_blank = sdf.parse(date_blank_string);
            if (date_sign.before(date_blank)){
                return null;
            }
        }
        return new UsernamePasswordAuthenticationToken(userid, null, null);
    }
}