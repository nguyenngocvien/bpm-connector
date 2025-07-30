package com.bpm.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bpm.core.entity.AuthUser;
import com.bpm.core.repository.AuthUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DefaultAdminInitializer implements CommandLineRunner {

	@Autowired
    private final AuthUserRepository authUserRepository;
	
	@Autowired
    private final PasswordEncoder passwordEncoder;

    public DefaultAdminInitializer(AuthUserRepository authUserRepository,
                                   PasswordEncoder passwordEncoder) {    	
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        String defaultUsername = "admin";
        String defaultPassword = "123456";
        String defaultRole = "ADMIN";

        Optional<AuthUser> existingUser = authUserRepository.findByUsername(defaultUsername);

        if (!existingUser.isPresent()) {
            AuthUser user = new AuthUser(defaultUsername, defaultPassword, defaultRole);
            authUserRepository.save(user, passwordEncoder);
        }
    }
}

