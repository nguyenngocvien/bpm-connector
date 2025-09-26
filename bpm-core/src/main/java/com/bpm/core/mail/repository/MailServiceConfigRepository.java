package com.bpm.core.mail.repository;

import com.bpm.core.mail.domain.MailServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailServiceConfigRepository extends JpaRepository<MailServiceConfig, Long> {

    List<MailServiceConfig> findByActiveTrue();
    
    MailServiceConfig findByServiceConfig_ServiceCode(String serviceCode);

    List<MailServiceConfig> findByServiceConfig_ServiceCodeAndActiveTrue(String serviceCode);
    
    MailServiceConfig findByIdAndActiveTrue(Long id);
}