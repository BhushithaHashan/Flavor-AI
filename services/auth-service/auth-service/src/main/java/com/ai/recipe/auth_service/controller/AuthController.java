package com.ai.recipe.auth_service.controller;

import com.ai.recipe.auth_service.dto.CreateProfileRequest;
import com.ai.recipe.auth_service.entity.User;
import com.ai.recipe.auth_service.model.AuthProvider;
import com.ai.recipe.auth_service.model.Role;
import com.ai.recipe.auth_service.security.JwtUtil;
import com.ai.recipe.auth_service.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ai.recipe.auth_service.clients.UserServiceClient;


import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Value("${USER_SERVICE_URL}")
    private String userServiceUrl;

    @Value("${INTERNAL_API_KEY}")
    private String internalApiKey;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final UserServiceClient userServiceClient;

    public AuthController(AuthService authService, JwtUtil jwtUtil,UserServiceClient userServiceClient) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.userServiceClient = userServiceClient;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {
        logger.info("=== REGISTER ENDPOINT HIT ===");
        logger.info("Request received: {}", req);

        String email = req.get("email");
        String password = req.get("password");
        String username = req.get("username"); // optional, can be null

        try {
            // Register in Auth DB
            User user = authService.register(email, password, username, Role.USER, AuthProvider.LOCAL);
            logger.info("User registered with id={}, email={}", user.getId(), user.getEmail());


            try {
                userServiceClient.createProfile(user.getId(),user.getEmail());
                logger.info("Profile created in User service for userId={}", user.getId());
            } catch (Exception ex) {
                logger.error("Failed to create profile for userId={}, rolling back", user.getId(), ex);
                // rollback auth user if profile creation fails
                authService.deleteUser(user.getId());
                throw new RuntimeException("Profile creation failed, registration rolled back.");
            }

            // Generate JWT
            String token = jwtUtil.generateToken(user.getId(), user.getEmail());

            //Return response
            return ResponseEntity.ok(Map.of(
                "token", token,
                "username", user.getUsername(),
                "email", user.getEmail(),
                "role", user.getRole()
            ));

        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.status(409).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        logger.info("=== TEST ENDPOINT HIT ===");
        return ResponseEntity.ok("Auth controller is working!");
    }
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        logger.info("=== VALIDATE TOKEN ENDPOINT HIT ===");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(400).body(Map.of("error", "Missing or invalid Authorization header"));
        }

        String token = authHeader.substring(7); // remove "Bearer "

        boolean isValid = jwtUtil.validateToken(token);
        if (!isValid) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }

        // Optionally, extract user info
        Long userId = jwtUtil.getUserIdFromToken(token);
        String email = jwtUtil.extractUsername(token);
        String username = authService.validateUser(email);

        return ResponseEntity.ok(Map.of(
            "valid", true,
            "username", username,
            "email", email
        ));
    }

}
