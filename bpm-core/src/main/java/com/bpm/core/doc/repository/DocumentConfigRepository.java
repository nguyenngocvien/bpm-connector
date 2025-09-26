package com.bpm.core.doc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bpm.core.doc.domain.DocumentServiceConfig;

public interface DocumentConfigRepository extends JpaRepository<DocumentServiceConfig, Long> {

    List<DocumentServiceConfig> findByActiveTrue();

    List<DocumentServiceConfig> findByTemplateId(Long templateId);

    List<DocumentServiceConfig> findByServerIdAndAuthId(Long serverId, Long authId);
}
