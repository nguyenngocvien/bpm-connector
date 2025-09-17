package com.bpm.core.auth.provider;

import com.bpm.core.auth.domain.AuthProperties;
import com.bpm.core.auth.domain.AuthProvider;
import com.bpm.core.auth.domain.AuthResult;

public class JwtAuthProvider implements AuthProvider {

    private final AuthProperties authProperties;

    public JwtAuthProvider(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public boolean supports(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    @Override
    public AuthResult authenticate(String authHeader) {
        try {
            String token = authHeader.substring(7).trim();
            // Simple token format: "username.jwtSecret"
            String[] parts = token.split("\\.");
            if (parts.length == 2) {
                String username = parts[0];
                String secret = parts[1];
                if (secret.equals(authProperties.getJwtSecret())) {
                    return AuthResult.success(username);
                } else {
                    return AuthResult.fail("Invalid JWT secret");
                }
            } else {
                return AuthResult.fail("Invalid token format");
            }
        } catch (Exception e) {
            return AuthResult.fail("JWT parsing error: " + e.getMessage());
        }
    }
}