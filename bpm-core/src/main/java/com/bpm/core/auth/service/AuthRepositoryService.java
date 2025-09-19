package com.bpm.core.auth.service;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.repository.AuthConfigRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class AuthRepositoryService {

    private final AuthConfigRepository authRepository;

    public AuthRepositoryService(AuthConfigRepository authRepository) {
        this.authRepository = authRepository;
    }

    public List<AuthConfig> getAllAuthConfigs() {
        return authRepository.findAll();
    }

    public AuthConfig getAuthConfigById(Long id) {
        return authRepository.findById(id)
        		.orElseThrow(() -> new UsernameNotFoundException("Credential not found: " + id));
    }

    public AuthConfig getAuthConfigByName(String name) {
    	AuthConfig config = authRepository.findByName(name)
    			.orElseThrow(() -> new UsernameNotFoundException("Credential not found: " + name)); 
    	
        return config;
    }

    public AuthConfig saveAuthConfig(AuthConfig config) {
        AuthConfig saved = authRepository.save(config);
        if (saved != null) {
            return saved;
        }
        throw new RuntimeException("Failed to save Credential: " + config.getName());
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
