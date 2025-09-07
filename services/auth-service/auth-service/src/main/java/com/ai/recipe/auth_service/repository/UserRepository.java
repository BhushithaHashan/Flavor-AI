package com.ai.recipe.auth_service.repository;

import com.ai.recipe.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find by email (used for login/register)
    Optional<User> findByEmail(String email);

    // Optional: find by username
    Optional<User> findByUsername(String username);
}
