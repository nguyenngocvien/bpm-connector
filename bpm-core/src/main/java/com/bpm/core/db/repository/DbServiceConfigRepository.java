package com.bpm.core.db.repository;

import com.bpm.core.db.domain.DbServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DbServiceConfigRepository extends JpaRepository<DbServiceConfig, Long> {
    List<DbServiceConfig> findByEnabledTrue();
}
