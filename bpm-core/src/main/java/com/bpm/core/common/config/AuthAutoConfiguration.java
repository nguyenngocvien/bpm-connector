package com.bpm.core.common.config;

import com.bpm.core.auth.provider.BasicAuthProvider;
import com.bpm.core.auth.provider.JwtAuthProvider;
import com.bpm.core.auth.service.AuthManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthAutoConfiguration {

    @Bean
    public AuthManager authManager(BasicAuthProvider basicAuthProvider,
                                   JwtAuthProvider jwtAuthProvider) {
        return new AuthManager(basicAuthProvider, jwtAuthProvider);
    }
}
