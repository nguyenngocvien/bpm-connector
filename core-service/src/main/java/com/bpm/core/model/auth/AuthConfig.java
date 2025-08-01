package com.bpm.core.model.auth;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthConfig {

    private Integer id;
    private String name;
    private AuthType authType;

    private String username;
    private String password;

    private String token;

    private String apiKeyHeader;
    private String apiKeyValue;

    private String oauth2ClientId;
    private String oauth2ClientSecret;
    private String oauth2TokenUrl;

    private String role;
    private String scope;

    @Default
    private boolean active = true;

    @Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
