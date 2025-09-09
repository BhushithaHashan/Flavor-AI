package com.ai.recipe.user_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${INTERNAL_API_KEY}")
    private String internalApiKey;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                
                if (request.getRequestURI().equals("/api/user/create-profile")) {
                    String secret = request.getHeader("X-Internal-Secret");
                    if (secret == null || !secret.equals(internalApiKey)) {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        return false;
                    }
                }
                return true;
            }
        });
    }
}
