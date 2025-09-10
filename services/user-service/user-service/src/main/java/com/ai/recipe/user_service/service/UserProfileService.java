package com.ai.recipe.user_service.service;

import com.ai.recipe.user_service.dto.UpdateProfileRequest;
import com.ai.recipe.user_service.entity.UserProfile;
import com.ai.recipe.user_service.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    /**
     * Get user profile by authUserId
     */
    public UserProfile getProfile(Long authUserId) {
        return repository.findByAuthUserId(authUserId)
                .orElseThrow(() -> new RuntimeException("Profile not found for user " + authUserId));
    }

    /**
     * Create or update user profile
     */
    @Transactional
    public UserProfile createOrUpdateProfile(Long authUserId, UpdateProfileRequest request) {
        Optional<UserProfile> existingOpt = repository.findByAuthUserId(authUserId);
        

        UserProfile profile;
        if (existingOpt.isPresent()) {
            // Update existing profile
            profile = existingOpt.get();
            profile.setFullName(request.getFullName());
            profile.setBio(request.getBio());
            profile.setAvatarUrl(request.getAvatarUrl());
            profile.setPreferences(request.getPreferences()); // JSON string
            profile.setRestrictions(request.getRestrictions());     // JSON string
        } else {
            // Create new profile
            profile = UserProfile.builder()
                    .authUserId(authUserId)
                    .fullName(request.getFullName())
                    .bio(request.getBio())
                    .avatarUrl(request.getAvatarUrl())
                    .preferences(request.getPreferences())
                    .Restrictions(request.getRestrictions())
                    .build();
        }

        return repository.save(profile);
    }
}
