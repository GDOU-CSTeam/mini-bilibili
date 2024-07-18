package com.sky.common.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class WebUtils {
    public static void renderString(HttpServletResponse response, String string) throws IOException {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
    }
}
