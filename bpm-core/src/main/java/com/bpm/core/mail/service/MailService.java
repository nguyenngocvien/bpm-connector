package com.bpm.core.mail.service;

import com.bpm.core.mail.domain.MailServiceConfig;
import com.bpm.core.mail.repository.MailRepository;

import java.util.List;
import java.util.Optional;

public class MailService {

    private final MailRepository mailRepository;

    public MailService(MailRepository mailRepository) {
        this.mailRepository = mailRepository;
    }

    public List<MailServiceConfig> getAllMailConfigs() {
        return mailRepository.findAll();
    }

    public Optional<MailServiceConfig> getMailConfigById(Long configId) {
        return mailRepository.findById(configId);
    }

    public MailServiceConfig saveMailConfig(MailServiceConfig config) {
        int affected = mailRepository.save(config);
        if (affected > 0) {
            return mailRepository.findById(config.getConfigId()).orElse(config);
        }
        throw new RuntimeException("Failed to save MailServiceConfig with configId=" + config.getConfigId());
    }

    public boolean deleteMailConfig(Long configId) {
        return mailRepository.deleteById(configId) > 0;
    }

    public boolean isActive(Long configId) {
        return mailRepository.findById(configId)
                .map(MailServiceConfig::getActive)
                .orElse(false);
    }
}
