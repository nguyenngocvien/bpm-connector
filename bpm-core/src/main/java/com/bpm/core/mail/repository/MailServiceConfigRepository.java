package com.bpm.core.mail.repository;

import com.bpm.core.mail.domain.MailServiceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MailServiceConfigRepository extends JpaRepository<MailServiceConfig, Long> {

    List<MailServiceConfig> findByActiveTrue();
}
