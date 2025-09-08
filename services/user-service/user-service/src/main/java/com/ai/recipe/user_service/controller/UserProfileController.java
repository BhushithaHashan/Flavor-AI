package com.ai.recipe.user_service.controller;

import com.ai.recipe.user_service.dto.UpdateProfileRequest;
import com.ai.recipe.user_service.dto.UserProfileResponse;
import com.ai.recipe.user_service.entity.UserProfile;
import com.ai.recipe.user_service.service.UserProfileService;
import com.ai.recipe.user_service.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

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
        UserProfile profile = profileService.createOrUpdateProfile(authUserId, request);
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
                .allergies(profile.getAllergies())
                .build();
    }
}
