package com.ai.recipe.user_service.controller;

import com.ai.recipe.user_service.dto.CreateProfileRequest;
import com.ai.recipe.user_service.dto.UpdateProfileRequest;
import com.ai.recipe.user_service.dto.UserProfileResponse;
import com.ai.recipe.user_service.entity.UserProfile;
import com.ai.recipe.user_service.service.UserProfileService;
import com.ai.recipe.user_service.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {
    private static final Logger logger = LoggerFactory.getLogger(UserProfileController.class);
    private final UserProfileService profileService;
    private final JwtUtil jwtUtil;

    public UserProfileController(UserProfileService profileService, JwtUtil jwtUtil) {
        this.profileService = profileService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Get current user's profile
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@RequestHeader("Authorization") String authHeader) {
        Long authUserId = extractAuthUserId(authHeader);
        
        UserProfile profile = profileService.getProfile(authUserId);
        return ResponseEntity.ok(convertToResponse(profile));
    }

    /**
     * Create or update current user's profile
     */
    @PutMapping("/profile")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateProfileRequest request) {

        Long authUserId = extractAuthUserId(authHeader);
        logger.info("AuthUserId extracted from token: {}", authUserId);
        UserProfile profile = profileService.createOrUpdateProfile(authUserId, request);
        return ResponseEntity.ok(convertToResponse(profile));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("User Service is working!");
    }

    @PostMapping("/create-profile")
    public ResponseEntity<UserProfileResponse> createProfile(@RequestBody CreateProfileRequest request) {
        // No token validation, since only Auth service will call this
        UserProfile profile = profileService.createOrUpdateProfile(
                request.getAuthUserId(),
                UpdateProfileRequest.builder()
                    .fullName(null)        // default empty fields
                    .bio(null)
                    .avatarUrl(null)
                    .preferences("{}")     // maybe an empty JSON
                    .Restrictions("{}")
                    .build()
        );

        return ResponseEntity.ok(convertToResponse(profile));
    }



    /**
     * Helper method to extract authUserId from JWT
     * Throws 401 automatically if invalid
     */
    private Long extractAuthUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }

        String token = authHeader.replace("Bearer ", "");
        if (!jwtUtil.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }

        return jwtUtil.getUserIdFromToken(token);
    }

    /**
     * Convert entity to DTO for response
     */
    private UserProfileResponse convertToResponse(UserProfile profile) {
        return UserProfileResponse.builder()
                .fullName(profile.getFullName())
                .bio(profile.getBio())
                .avatarUrl(profile.getAvatarUrl())
                .preferences(profile.getPreferences())
                .Restrictions(profile.getRestrictions())
                .build();
    }
}
