package com.bpm.core.common.config;

import com.bpm.core.auth.repository.AuthRepository;
import com.bpm.core.cache.AuthServiceCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheAutoConfiguration {

    @Bean
    public AuthServiceCache authServiceCache(AuthRepository authRepository) {
        return new AuthServiceCache(authRepository);
    }
}
