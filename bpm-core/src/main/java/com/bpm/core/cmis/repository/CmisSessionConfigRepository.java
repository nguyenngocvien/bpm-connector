package com.bpm.core.cmis.repository;

import com.bpm.core.cmis.domain.CmisSessionConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CmisSessionConfigRepository extends JpaRepository<CmisSessionConfig, Long> {
	
	Optional<CmisSessionConfig> findByIdAndActiveTrue(Long id);

    Optional<CmisSessionConfig> findByNameAndActiveTrue(String name);

    Optional<CmisSessionConfig> findFirstByActiveTrue();
}
