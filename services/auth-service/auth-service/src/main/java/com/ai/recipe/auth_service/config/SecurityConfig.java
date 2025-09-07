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

        http
            .csrf(csrf -> csrf.disable()) // stateless API, disable CSRF
            .authorizeHttpRequests(auth -> {
                logger.info("Configuring authorization rules");

                // Public endpoints
                auth.requestMatchers("/api/auth/register").permitAll();
                auth.requestMatchers("/api/auth/login").permitAll();
                auth.requestMatchers("/api/auth/test").permitAll();

                // Everything else under /api/auth/** requires authentication
                auth.requestMatchers("/api/auth/**").authenticated();

                logger.info("All other requests require authentication");
            });

        return http.build();
    }
}
