package com.sky.common.config;

import com.sky.common.filter.TokenAuthenticationFilter;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class is used to configure the security settings for the application.
 * It uses Spring Security's annotations and beans to set up the security filter chain.
 */
@Configuration
@EnableWebSecurity // Enable Spring Security's web security support
@RequiredArgsConstructor // Lombok annotation to generate a constructor for all final fields, with a parameter for each
@EnableMethodSecurity(securedEnabled = true) // Enable Spring Security's method level security
public class SecurityConfiguration {

    // Spring Security's AccessDeniedHandler, used to handle access denied exceptions
    @Resource
    AccessDeniedHandler accessDeniedHandler;

    // Spring Security's AuthenticationEntryPoint, used to commence an authentication scheme
    @Resource
    AuthenticationEntryPoint authenticationEntryPoint;

    /**
     * Bean for BCryptPasswordEncoder, used for password encoding.
     * @return a new BCryptPasswordEncoder.
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the SecurityFilterChain which is used to apply security to the requests.
     * @param http HttpSecurity instance used to build the security filter chain
     * @return the SecurityFilterChain
     * @throws Exception if an error occurs when configuring the security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .requestMatchers("/**").permitAll() // Allow all requests to pass the security filter
                                // Uncomment and modify the lines below to add your own authorization rules.
                                // .requestMatchers("/test_security").hasRole("ADMIN")
                                // .requestMatchers("/test_security2").hasRole("USER")
                                .anyRequest().authenticated() // Any other request needs to be authenticated
                )
                .exceptionHandling((exceptions) ->
                        exceptions
                                .authenticationEntryPoint(authenticationEntryPoint)
                                // Set the authentication entry point
                                .accessDeniedHandler(accessDeniedHandler)// Set the access denied handler
                )
                .sessionManagement((session) ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // Set the session creation policy to stateless
                )
                .addFilterBefore(new TokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // Add the TokenAuthenticationFilter before the UsernamePasswordAuthenticationFilter
        http.csrf(AbstractHttpConfigurer::disable); // Disable CSRF protection
        return http.build(); // Build the SecurityFilterChain
    }
}
