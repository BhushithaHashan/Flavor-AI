package com.ai.recipe.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private String fullName;
    private String bio;
    private String avatarUrl;
    private String preferences; // JSON string
    private String allergies;   // JSON string
}
