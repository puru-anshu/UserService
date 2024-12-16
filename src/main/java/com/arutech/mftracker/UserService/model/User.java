package com.arutech.mftracker.UserService.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthProvider authProvider;

    @Column(name = "google_sub")
    private String googleSub;

    @Column(name = "is_enabled")
    private boolean enabled = true;

    public enum AuthProvider {
        LOCAL, GOOGLE
    }
}