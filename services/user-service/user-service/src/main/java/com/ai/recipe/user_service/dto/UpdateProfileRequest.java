package com.ai.recipe.user_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateProfileRequest {
    private String fullName;
    private String bio;
    private String avatarUrl;
    private String preferences; 
    private String Restrictions;   
}
