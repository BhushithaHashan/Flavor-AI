package com.ai.recipe.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("=== SECURITY CONFIG LOADING ===");
        logger.info("Configuring security filter chain...");
        
        http
            .csrf(csrf -> {
                logger.info("Disabling CSRF");
                csrf.disable();
            }) 
            .authorizeHttpRequests(auth -> {
                logger.info("Configuring authorization rules");
                logger.info("Permitting all requests to /api/auth/**");
                auth.requestMatchers("/api/auth/**").permitAll();
                logger.info("All other requests require authentication");
                auth.anyRequest().authenticated();
            });
        
        logger.info("Security configuration complete");
        return http.build();
    }
}