package com.ai.recipe.auth_service.clients;

import com.ai.recipe.auth_service.dto.CreateProfileRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${USER_SERVICE_URL}")
    private String userServiceUrl;

    @Value("${INTERNAL_API_KEY}")
    private String internalApiKey;

    public UserServiceClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public void createProfile(Long authUserId, String email) {
        CreateProfileRequest request = new CreateProfileRequest();
        request.setAuthUserId(authUserId);
        request.setEmail(email);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Internal-Secret", internalApiKey);

        HttpEntity<CreateProfileRequest> entity = new HttpEntity<>(request, headers);
        restTemplate.postForEntity(userServiceUrl + "/api/user/create-profile", entity, Void.class);
    }
}
