package com.bpm.auth.store;

import org.mindrot.jbcrypt.BCrypt;

import com.bpm.core.repository.ServiceAuthRepository;

public class UserCredentialStore {

    private final ServiceAuthRepository authRepository;

    public UserCredentialStore(ServiceAuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public boolean isValid(String username, String rawPassword) {
        return authRepository.findByUsername(username)
                .map(user -> {
                    String hashedPassword = user.getPassword();
                    return BCrypt.checkpw(rawPassword, hashedPassword);
                })
                .orElse(false);
    }
}