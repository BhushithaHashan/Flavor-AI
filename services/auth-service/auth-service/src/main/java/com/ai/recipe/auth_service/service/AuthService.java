package com.ai.recipe.auth_service.service;

import com.ai.recipe.auth_service.entity.User;
import com.ai.recipe.auth_service.model.AuthProvider;
import com.ai.recipe.auth_service.model.Role;
import com.ai.recipe.auth_service.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register user
    public User register(String email, String password, String username, Role role, AuthProvider provider) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already taken");
        }

        User user = User.builder()
                .email(email)
                .username(username)
                .password(password != null ? encoder.encode(password) : null)
                .role(role)
                .provider(provider)
                .build();

        return userRepository.save(user);
    }

    // Validate login credentials for LOCAL users
    public User validateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> u.getProvider() == AuthProvider.LOCAL) // Only local users validate password
                .filter(u -> encoder.matches(password, u.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }
}
