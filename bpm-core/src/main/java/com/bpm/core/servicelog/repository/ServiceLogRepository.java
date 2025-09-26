package com.bpm.core.servicelog.repository;

import com.bpm.core.servicelog.domain.ServiceLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceLogRepository extends JpaRepository<ServiceLog, Long> {

    List<ServiceLog> findByServiceCodeOrderByCreatedAtDesc(String serviceCode);

    List<ServiceLog> findTop10ByOrderByCreatedAtDesc();
}
