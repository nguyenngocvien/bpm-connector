package com.bpm.core.common.config;

import com.bpm.core.auth.domain.AuthProperties;
import com.bpm.core.auth.provider.BasicAuthProvider;
import com.bpm.core.auth.provider.JwtAuthProvider;
import com.bpm.core.auth.repository.AuthRepository;
import com.bpm.core.auth.service.AuthManager;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@AutoConfiguration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthAutoConfiguration {
	
	@Bean
    public BasicAuthProvider basicAuthProvider(AuthProperties properties) {
        return new BasicAuthProvider(properties);
    }

    @Bean
    public JwtAuthProvider jwtAuthProvider(AuthProperties properties) {
        return new JwtAuthProvider(properties);
    }

    @Bean
    public AuthManager authManager(BasicAuthProvider basicAuthProvider,
                                   JwtAuthProvider jwtAuthProvider) {
        return new AuthManager(basicAuthProvider, jwtAuthProvider);
    }
    
    @Bean
    public AuthRepository authRepository(JdbcTemplate jdbcTemplate) {
        return new AuthRepository(jdbcTemplate);
    }
}
