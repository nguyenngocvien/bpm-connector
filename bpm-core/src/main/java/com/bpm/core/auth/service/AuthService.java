package com.bpm.core.auth.service;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.repository.AuthRepository;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public List<AuthConfig> getAllAuthConfigs() {
        return authRepository.findAll();
    }

    public Optional<AuthConfig> getAuthConfigById(Integer id) {
        return authRepository.findById(id);
    }

    public AuthConfig getAuthConfigByName(String name) {
    	AuthConfig config = authRepository.findByName(name)
    			.orElseThrow(() -> new UsernameNotFoundException("User not found: " + name)); 
    	
        return config;
    }

    public AuthConfig saveAuthConfig(AuthConfig config) {
        int affected = authRepository.save(config);
        if (affected > 0) {
            if (config.getId() != null) {
                return authRepository.findById(config.getId()).orElse(config);
            } else {
                return authRepository.findByName(config.getName()).orElse(config);
            }
        }
        throw new RuntimeException("Failed to save AuthConfig: " + config.getName());
    }

    public boolean deleteAuthConfig(Long id) {
        return authRepository.deleteById(id) > 0;
    }

    public boolean isActiveAuth(String name) {
        return authRepository.findByName(name)
                .map(AuthConfig::isActive)
                .orElse(false);
    }
}
