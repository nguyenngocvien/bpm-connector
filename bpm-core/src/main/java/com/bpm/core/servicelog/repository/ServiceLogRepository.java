package com.bpm.core.servicelog.repository;

import com.bpm.core.servicelog.domain.ServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceLogRepository extends JpaRepository<ServiceLog, Long> {

    List<ServiceLog> findByServiceCodeOrderByCreatedAtDesc(String serviceCode);

    List<ServiceLog> findTop10ByOrderByCreatedAtDesc();
}
