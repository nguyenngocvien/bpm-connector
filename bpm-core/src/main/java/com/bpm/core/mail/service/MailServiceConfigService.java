package com.bpm.core.mail.service;

import com.bpm.core.mail.domain.MailServiceConfig;
import com.bpm.core.mail.repository.MailServiceConfigRepository;

import java.util.List;

public class MailServiceConfigService {

    private final MailServiceConfigRepository repository;
    
    public MailServiceConfigService(MailServiceConfigRepository repository) {
    	this.repository = repository;
    }

    public List<MailServiceConfig> getAllConfigs() {
        return repository.findAll();
    }

    public MailServiceConfig getConfigById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MailServiceConfig not found with ID: " + id));
    }

    public List<MailServiceConfig> getActiveConfigs() {
        return repository.findByActiveTrue();
    }

    public MailServiceConfig saveConfig(MailServiceConfig config) {
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
