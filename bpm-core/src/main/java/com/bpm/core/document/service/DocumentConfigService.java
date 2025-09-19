package com.bpm.core.document.service;

import com.bpm.core.document.domain.DocumentServiceConfig;
import com.bpm.core.document.repository.DocumentConfigRepository;

import java.time.LocalDateTime;
import java.util.List;

public class DocumentConfigService {

    private final DocumentConfigRepository repository;
    
    public DocumentConfigService(DocumentConfigRepository repository) {
		this.repository = repository;
	}

    public List<DocumentServiceConfig> getAllConfigs() {
        return repository.findAll();
    }

    public DocumentServiceConfig getConfigById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DocumentServiceConfig not found with ID: " + id));
    }

    public List<DocumentServiceConfig> getActiveConfigs() {
        return repository.findByActiveTrue();
    }

    public List<DocumentServiceConfig> getConfigsByTemplateId(Long templateId) {
        return repository.findByTemplateId(templateId);
    }

    public List<DocumentServiceConfig> getConfigsByServerAndAuth(Long serverId, Long authId) {
        return repository.findByServerIdAndAuthId(serverId, authId);
    }

    public DocumentServiceConfig saveConfig(DocumentServiceConfig config) {
        config.setUpdatedAt(LocalDateTime.now());
        if (config.getCreatedAt() == null) {
            config.setCreatedAt(LocalDateTime.now());
        }
        return repository.save(config);
    }

    public boolean deleteConfig(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
