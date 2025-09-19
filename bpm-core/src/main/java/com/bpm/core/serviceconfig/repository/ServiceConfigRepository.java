package com.bpm.core.serviceconfig.repository;

import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ServiceConfigRepository extends JpaRepository<ServiceConfig, Long> {

    Optional<ServiceConfig> findByServiceCode(String serviceCode);

    List<ServiceConfig> findByServiceType(ServiceType type);

    List<ServiceConfig> findByActiveTrue();

    // --- search by keyword ---
    @Query("""
           SELECT sc FROM ServiceConfig sc
           WHERE LOWER(sc.serviceName) LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(sc.serviceCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(sc.serviceDescription) LIKE LOWER(CONCAT('%', :keyword, '%'))
           """)
    List<ServiceConfig> searchByKeyword(String keyword);

    // --- toggle log ---
    @Transactional
    @Modifying
    @Query("UPDATE ServiceConfig sc SET sc.logEnabled = :enabled WHERE sc.id = :id")
    void enableLog(Long id, boolean enabled);

    // --- toggle active ---
    @Transactional
    @Modifying
    @Query("UPDATE ServiceConfig sc SET sc.active = :active WHERE sc.id = :id")
    void setActive(Long id, boolean active);
}
