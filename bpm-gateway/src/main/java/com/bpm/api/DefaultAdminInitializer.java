package com.bpm.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.domain.AuthType;
import com.bpm.core.auth.service.AuthRepositoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultAdminInitializer implements CommandLineRunner {

    private final AuthRepositoryService authRepository;

    @Value("${app.auth.default.username:admin}")
    private String defaultUsername;

    @Value("${app.auth.default.password:123456}")
    private String defaultPassword;

    @Value("${app.auth.default.role:ADMIN}")
    private String defaultRole;
    
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Optional<AuthConfig> existing = authRepository.getAuthLogin(defaultUsername);

        if (existing.isPresent()) {
            log.info("Default admin '{}' already exists.", defaultUsername);
            return;
        }

        AuthConfig admin = new AuthConfig();
        admin.setName(defaultUsername);
        admin.setAuthType(AuthType.BASIC);
        admin.setUsername(defaultUsername);
        admin.setPassword(passwordEncoder.encode(defaultPassword)); // TODO: encode password!
        admin.setRole(defaultRole);
        admin.setActive(true);

        authRepository.saveAuthLogin(admin);
        log.info("Default admin '{}' created.", defaultUsername);
    }
}
