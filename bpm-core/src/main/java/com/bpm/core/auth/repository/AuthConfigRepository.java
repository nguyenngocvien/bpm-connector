package com.bpm.core.auth.repository;

import com.bpm.core.auth.domain.AuthConfig;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthConfigRepository extends JpaRepository<AuthConfig, Long> {
    Optional<AuthConfig> findByName(String name);
}
