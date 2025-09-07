package com.ai.recipe.user_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "user_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID from Auth Service
    @Column(nullable = false, unique = true)
    private Long authUserId;

    private String fullName;
    private String bio;
    private String avatarUrl;

    @Column(columnDefinition = "jsonb")
    private String preferences; // JSON string for dietary preferences

    @Column(columnDefinition = "jsonb")
    private String allergies; // JSON string for allergies

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}
