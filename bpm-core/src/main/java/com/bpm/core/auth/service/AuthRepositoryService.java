package com.bpm.core.auth.service;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.repository.AuthConfigRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public class AuthRepositoryService {

    private final AuthConfigRepository authRepository;

    public AuthRepositoryService(AuthConfigRepository authRepository) {
        this.authRepository = authRepository;
    }

    public List<AuthConfig> getAllAuthConfigs() {
        return authRepository.findAll();
    }

    public Optional<AuthConfig> getAuthConfigById(Long id) {
        return authRepository.findById(id);
    }

    public AuthConfig getAuthConfigByName(String name) {
    	AuthConfig config = authRepository.findByName(name)
    			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + name)); 
    	
        return config;
    }

    public AuthConfig saveAuthConfig(AuthConfig config) {
        AuthConfig saved = authRepository.save(config);
        if (saved != null) {
            return saved;
        }
        throw new RuntimeException("Failed to save AuthConfig: " + config.getName());
    }

    public boolean deleteAuthConfig(Long id) {
        if (authRepository.existsById(id)) {
            authRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean isActiveAuth(String name) {
        return authRepository.findByName(name)
                             .map(AuthConfig::isActive)
                             .orElse(false);
    }
}
