package com.bpm.core.auth.service;

import java.util.HashMap;
import java.util.Map;

import com.bpm.core.auth.domain.AuthProvider;
import com.bpm.core.auth.domain.AuthResult;
import com.bpm.core.auth.provider.BasicAuthProvider;
import com.bpm.core.auth.provider.JwtAuthProvider;

public class AuthManager {

    private final Map<String, AuthProvider> providers = new HashMap<>();

    public AuthManager(BasicAuthProvider basicAuthProvider, JwtAuthProvider jwtAuthProvider) {
        providers.put("basic", basicAuthProvider);
        providers.put("jwt", jwtAuthProvider);
    }

    public AuthResult authenticate(String authHeader) {
        if (authHeader == null || authHeader.trim().isEmpty()) {
            return AuthResult.fail("Missing Authorization header");
        }

        for (AuthProvider provider : providers.values()) {
            if (provider.supports(authHeader)) {
                return provider.authenticate(authHeader);
            }
        }
        return AuthResult.fail("Unsupported auth type in header");
    }
}
