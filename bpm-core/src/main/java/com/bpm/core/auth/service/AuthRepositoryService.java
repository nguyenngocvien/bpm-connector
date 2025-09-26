package com.bpm.core.auth.service;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.repository.AuthConfigRepository;
import com.bpm.core.common.util.AESCryptoHelper;

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

    public AuthConfig getAuthConfigById(Long id) {
        return authRepository.findById(id).map(auth -> {
            // Decrypt password
            auth.setPassword(AESCryptoHelper.decrypt(auth.getPassword()));
            return auth;
        }).orElseThrow(() -> new RuntimeException("Credential not found with id: " + id));
    }

    public AuthConfig getAuthConfigByName(String name) {
        return authRepository.findByName(name)
                .map(auth -> {
                    auth.setPassword(AESCryptoHelper.decrypt(auth.getPassword()));
                    return auth;
                })
                .orElseThrow(() -> new UsernameNotFoundException("Credential not found: " + name));
    }
    
    public Optional<AuthConfig> getAuthLogin(String username) {
        return authRepository.findByUsername(username);
    }

    public AuthConfig saveAuthLogin(AuthConfig config) {
        return authRepository.save(config);
    }
    
    public AuthConfig saveAuthConfig(AuthConfig config) {
    	// Encrypt password
        config.setPassword(AESCryptoHelper.encrypt(config.getPassword()));
        return authRepository.save(config);
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
