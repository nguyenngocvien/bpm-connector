package com.bpm.core.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bpm.core.entity.ServiceConfig;

public interface ServiceConfigRepository extends JpaRepository<ServiceConfig, Long> {
    Optional<ServiceConfig> findByServiceCodeAndActiveTrue(String serviceCode);
}