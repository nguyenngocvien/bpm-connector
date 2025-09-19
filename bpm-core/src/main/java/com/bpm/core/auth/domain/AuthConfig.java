package com.bpm.core.auth.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_auth_credentials")
public class AuthConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_type", nullable = false)
    private AuthType authType;

    private String username;
    private String password;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Column(name = "api_key_header")
    private String apiKeyHeader;

    @Column(name = "api_key_value", columnDefinition = "TEXT")
    private String apiKeyValue;

    @Column(name = "oauth2_client_id")
    private String oauth2ClientId;

    @Column(name = "oauth2_client_secret")
    private String oauth2ClientSecret;

    @Column(name = "oauth2_token_url", columnDefinition = "TEXT")
    private String oauth2TokenUrl;

    private String role;
    private String scope;

    @Builder.Default
    private boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
