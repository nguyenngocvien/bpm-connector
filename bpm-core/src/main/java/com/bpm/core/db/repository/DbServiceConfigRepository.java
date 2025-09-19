package com.bpm.core.db.repository;

import com.bpm.core.db.domain.DbServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DbServiceConfigRepository extends JpaRepository<DbServiceConfig, Long> {
    List<DbServiceConfig> findByEnabledTrue();
}
