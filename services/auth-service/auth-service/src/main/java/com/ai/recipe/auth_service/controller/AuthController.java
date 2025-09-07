package com.ai.recipe.auth_service.controller;

import com.ai.recipe.auth_service.entity.User;
import com.ai.recipe.auth_service.model.AuthProvider;
import com.ai.recipe.auth_service.model.Role;
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

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {
        logger.info("=== REGISTER ENDPOINT HIT ===");
        logger.info("Request received: {}", req);

        String email = req.get("email");
        String password = req.get("password");
        String username = req.get("username"); // optional, can be null

        try {
            User user = authService.register(email, password, username, Role.USER, AuthProvider.LOCAL);
            String token = jwtUtil.generateToken(user.getId(),user.getEmail());

        return ResponseEntity.ok(Map.of(
            // "id", user.getId(),            
            "token", token,
            "email", user.getEmail(),
            "username", user.getUsername(),
            "role", user.getRole()
        ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        logger.info("=== LOGIN ENDPOINT HIT ===");
        logger.info("Login request received: {}", req);

        String email = req.get("email");
        String password = req.get("password");

        try {
            User user = authService.validateUser(email, password);
            String token = jwtUtil.generateToken(user.getId(),user.getEmail());

            return ResponseEntity.ok(Map.of(
                // "id", user.getId(),            
                "token", token,
                "email", user.getEmail(),
                "username", user.getUsername(),
                "role", user.getRole()
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("=== TEST ENDPOINT HIT ===");
        return ResponseEntity.ok("Auth controller is working!");
    }
}
