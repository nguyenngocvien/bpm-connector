package com.bpm.core.db.repository;

import com.bpm.core.db.domain.DataSourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DataSourceRepository extends JpaRepository<DataSourceConfig, Long> {

    Optional<DataSourceConfig> findByName(String name);
    
    List<DataSourceConfig> findByActiveTrue();
}
