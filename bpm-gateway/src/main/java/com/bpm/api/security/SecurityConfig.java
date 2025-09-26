package com.bpm.api.security;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.service.AuthRepositoryService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final AuthRepositoryService authService;

    public SecurityConfig(AuthRepositoryService authService) {
        this.authService = authService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * DB-backed UserDetailsService
     * Supports multiple roles (comma-separated in AuthConfig.role)
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            AuthConfig auth = authService.getAuthLogin(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            // Roles are comma-separated in DB, e.g., "ADMIN,USER"
            String[] roles = auth.getRole() != null ? auth.getRole().split(",") : new String[]{};
            
            return User.builder()
                    .username(auth.getUsername())
                    .password(auth.getPassword())
                    .roles(roles)
                    .disabled(!auth.isActive())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST APIs
            .csrf(AbstractHttpConfigurer::disable)

            // Configure public and protected endpoints
            .authorizeHttpRequests(auth -> auth
                // Public Swagger/OpenAPI endpoints
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/configuration/**"
                ).permitAll()
                
                // Role-based access control
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("ADMIN", "USER")
                
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            // Enable HTTP Basic authentication
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
