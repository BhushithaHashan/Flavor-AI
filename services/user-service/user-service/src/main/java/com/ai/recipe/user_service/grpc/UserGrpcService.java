package com.ai.recipe.user_service.grpc;

import com.ai.recipe.user_service.proto.UserServiceGrpc;
import com.ai.recipe.user_service.proto.CreateProfileRequest;
import com.ai.recipe.user_service.proto.CreateProfileResponse;
import com.ai.recipe.user_service.service.UserProfileService;
import com.ai.recipe.user_service.dto.UpdateProfileRequest;
import com.ai.recipe.user_service.entity.UserProfile;
import com.ai.recipe.user_service.dto.UserProfileResponse;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class UserGrpcService extends UserServiceGrpc.UserServiceImplBase {

    private final UserProfileService userProfileService;

    public UserGrpcService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @Override
    public void createProfile(CreateProfileRequest request, StreamObserver<CreateProfileResponse> responseObserver) {
        boolean success = true;
        String message = "Profile created successfully";

        try {
            UserProfile profile = userProfileService.createOrUpdateProfile(
                Long.parseLong(request.getProfile().getId()),
                UpdateProfileRequest.builder()
                    .fullName(null)
                    .bio(null)
                    .avatarUrl(null)
                    .preferences("{}")
                    .allergies("{}")
                    .build()
            );
        } catch (Exception e) {
            success = false;
            message = e.getMessage();
        }

        CreateProfileResponse response = CreateProfileResponse.newBuilder()
            .setSuccess(success)
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
