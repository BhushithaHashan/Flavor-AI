package com.ai.recipe.auth_service.controller;

import com.ai.recipe.auth_service.entity.User;
import com.ai.recipe.auth_service.security.JwtUtil;
import com.ai.recipe.auth_service.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // ADD THIS LINE
    
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {
        logger.info("=== REGISTER ENDPOINT HIT ==="); // ADD THIS
        logger.info("Request received: {}", req); // ADD THIS
        
        User user = authService.register(req.get("username"), req.get("password"));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        logger.info("=== LOGIN ENDPOINT HIT ==="); // ADD THIS
        logger.info("Login request received: {}", req); // ADD THIS
        
        User user = authService.validateUser(req.get("username"), req.get("password"));
        String token = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }

    // ADD THIS TEST ENDPOINT
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("=== TEST ENDPOINT HIT ===");
        return ResponseEntity.ok("Auth controller is working!");
    }
}