package com.bpm.core.rest.repository;

import com.bpm.core.rest.domain.RestServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestServiceConfigRepository extends JpaRepository<RestServiceConfig, Long> {

    Optional<RestServiceConfig> findById(Long id);
    
    Optional<RestServiceConfig> findByIdAndServiceConfig_ActiveTrue(Long id);

    boolean existsByPathAndServerId(String path, Long serverId);
}
