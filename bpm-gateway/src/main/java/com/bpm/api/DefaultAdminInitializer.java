package com.bpm.api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bpm.core.entity.AuthUser;
import com.bpm.core.repository.AuthUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.jdbc.core.JdbcTemplate;

@Component
public class DefaultAdminInitializer implements CommandLineRunner {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public DefaultAdminInitializer(AuthUserRepository authUserRepository,
                                   PasswordEncoder passwordEncoder,
                                   JdbcTemplate jdbcTemplate) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        String defaultUsername = "admin";
        String defaultPassword = "123456";
        String defaultRole = "ADMIN";

        String sqlCheck = "SELECT COUNT(*) FROM isrv_auth WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sqlCheck, Integer.class, defaultUsername);

        if (count != null && count == 0) {
            AuthUser user = new AuthUser(defaultUsername, defaultPassword, defaultRole);
            authUserRepository.insert(user, passwordEncoder);
            System.out.println("✅ Admin user created: admin / 123456");
        } else {
            System.out.println("ℹ️ Admin user already exists, skip insert.");
        }
    }
}
