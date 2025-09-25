package com.bpm.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.domain.AuthType;
import com.bpm.core.auth.repository.AuthConfigRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DefaultAdminInitializer implements CommandLineRunner {

    private final AuthConfigRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.auth.default.username:admin}")
    private String defaultUsername;

    @Value("${app.auth.default.password:123456}")
    private String defaultPassword;
    
    @Value("${app.auth.default.role:ADMIN}")
    private String defaultRole;

    @Autowired
    public DefaultAdminInitializer(AuthConfigRepository authRepository, PasswordEncoder passwordEncoder) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Optional<AuthConfig> authConfig = authRepository.findByName(defaultUsername);
        if (authConfig.isPresent()) {
            System.out.println("[INFO] Default admin already exists.");
            return;
        }

        AuthConfig admin = new AuthConfig();
        admin.setName(defaultUsername);
        admin.setAuthType(AuthType.BASIC);
        admin.setUsername(defaultUsername);
        admin.setPassword(passwordEncoder.encode(defaultPassword));
        admin.setRole(defaultRole);
        admin.setActive(true);

        authRepository.save(admin);
        System.out.println("[INFO] Default admin user created.");
    }
}