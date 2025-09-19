package com.bpm.core.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bpm.core.document.domain.DocumentServiceConfig;

public interface DocumentConfigRepository extends JpaRepository<DocumentServiceConfig, Long> {

    List<DocumentServiceConfig> findByActiveTrue();

    List<DocumentServiceConfig> findByTemplateId(Long templateId);

    List<DocumentServiceConfig> findByServerIdAndAuthId(Long serverId, Long authId);
}
