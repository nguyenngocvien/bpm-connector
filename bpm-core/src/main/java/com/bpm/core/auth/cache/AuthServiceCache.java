package com.bpm.core.auth.cache;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.domain.AuthType;
import com.bpm.core.auth.repository.AuthRepository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AuthServiceCache {

    private final AuthRepository authRepository;

    // Simple in-memory cache
    private final ConcurrentHashMap<Integer, AuthConfig> authCacheById = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AuthConfig> authCacheByName = new ConcurrentHashMap<>();

    public AuthServiceCache(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public Optional<AuthConfig> getAuthById(Integer id) {
        AuthConfig config = authCacheById.computeIfAbsent(id, k ->
            authRepository.findById(k).filter(this::validate).orElse(null));
        if (config != null) authCacheByName.putIfAbsent(config.getName(), config);
        return Optional.ofNullable(config);
    }

    public Optional<AuthConfig> getAuthByName(String name) {
        AuthConfig config = authCacheByName.computeIfAbsent(name, k ->
            authRepository.findByName(k).filter(this::validate).orElse(null));
        if (config != null) authCacheById.putIfAbsent(config.getId(), config);
        return Optional.ofNullable(config);
    }

    // Validate auth config based on type
    public boolean validate(AuthConfig config) {
        if (config == null || !config.isActive()) return false;

        AuthType type = config.getAuthType();
        switch (type) {
            case BASIC:
                return config.getUsername() != null && config.getPassword() != null;
            case BEARER:
                return config.getToken() != null;
            case API_KEY:
                return config.getApiKeyHeader() != null && config.getApiKeyValue() != null;
            case OAUTH2:
                return config.getToken() != null || (config.getOauth2TokenUrl() != null);
            case NONE:
            default:
                return true;
        }
    }

    public void clearCache() {
        authCacheById.clear();
        authCacheByName.clear();
    }
}
