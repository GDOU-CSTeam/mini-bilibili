package com.bili.admin.config;


import com.bili.common.filter.JwtAuthenticationTokenFilter;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity// 开启网络安全注解
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig{

    @Resource
    AccessDeniedHandler accessDeniedHandler;
    @Resource
    AuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    //创建BCryptPasswordEncoder注入容器
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //配置安全过滤器链。
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 配置请求授权规则
        http
                .authorizeHttpRequests((authorize) -> authorize
                         // 允许访问Swagger的相关URL
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**","/doc.html/**").permitAll()
                        //登出接口要验证
                        .requestMatchers("/login/logout").authenticated()
                        //允许访问登录接口
                        .requestMatchers("/login/**").permitAll()
                        .anyRequest().authenticated()
                )
                // 配置异常处理
                .exceptionHandling((exceptions) ->
                        exceptions
                                // 设置未通过身份验证时的入口点
                                .authenticationEntryPoint(authenticationEntryPoint)
                                // 设置拒绝访问时的处理器
                                .accessDeniedHandler(accessDeniedHandler)
                )
                // 配置会话管理策略
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // 在UsernamePasswordAuthenticationFilter之前添加一个过滤器来处理令牌身份验证
                .addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 禁用CSRF保护
        http.csrf(AbstractHttpConfigurer::disable);

        // 返回构建好的安全过滤器链
        return http.build();
    }
}