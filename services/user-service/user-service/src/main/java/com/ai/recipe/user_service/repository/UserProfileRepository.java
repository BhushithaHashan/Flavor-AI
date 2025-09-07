package com.ai.recipe.user_service.repository;

import com.ai.recipe.user_service.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    // Find a profile by the Auth Service user ID
    Optional<UserProfile> findByAuthUserId(Long authUserId);

    // delete or check existence
    boolean existsByAuthUserId(Long authUserId);
}
