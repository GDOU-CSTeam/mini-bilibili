package com.sky.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.common.utils.Result;
import com.sky.common.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Result.forbidden("无法访问"));
        WebUtils.renderString(response,json);
    }
}
