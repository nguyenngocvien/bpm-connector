package com.bpm.core.serviceconfig.service;

import com.bpm.core.db.service.DbServiceConfigService;
import com.bpm.core.document.domain.DocumentServiceConfig;
import com.bpm.core.document.repository.DocumentConfigRepository;
import com.bpm.core.mail.service.MailServiceConfigService;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.service.RestServiceConfigService;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;
import com.bpm.core.serviceconfig.repository.ServiceConfigRepository;

import java.util.List;

public class ServiceConfigRepositoryService {

    private final ServiceConfigRepository serviceConfigRepo;
    private final DbServiceConfigService dbService;
    private final RestServiceConfigService restService;
    private final MailServiceConfigService mailService;
    private final DocumentConfigRepository docRepository;

    public ServiceConfigRepositoryService(ServiceConfigRepository serviceConfigRepo,
                                          DbServiceConfigService dbService,
                                          RestServiceConfigService restService,
                                          MailServiceConfigService mailService,
                                          DocumentConfigRepository docRepository) {
        this.serviceConfigRepo = serviceConfigRepo;
        this.dbService = dbService;
        this.restService = restService;
        this.mailService = mailService;
        this.docRepository = docRepository;
    }

    public List<ServiceConfig> findAll(String keyword) {
        return (keyword != null && !keyword.trim().isEmpty())
                ? serviceConfigRepo.searchByKeyword(keyword)
                : serviceConfigRepo.findAll();
    }

    public ServiceConfig findById(Long id) {
        ServiceConfig config = serviceConfigRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + id));
        enrichConfig(config);
        return config;
    }

    public ServiceConfig findByCode(String serviceCode) {
        ServiceConfig config = serviceConfigRepo.findByServiceCode(serviceCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Code: " + serviceCode));
        enrichConfig(config);
        return config;
    }

    public void save(ServiceConfig config) {
        serviceConfigRepo.save(config);
        saveDetailConfig(config);
    }

    public void delete(Long id) {
        if (!serviceConfigRepo.existsById(id)) {
            throw new IllegalArgumentException("Service not found: " + id);
        }
        serviceConfigRepo.deleteById(id);
    }

    public void toggleLog(Long id, boolean enabled) {
        if (!serviceConfigRepo.existsById(id)) {
            throw new IllegalArgumentException("Service not found: " + id);
        }
        serviceConfigRepo.enableLog(id, enabled);
    }

    public void toggleActive(Long id, boolean active) {
        if (!serviceConfigRepo.existsById(id)) {
            throw new IllegalArgumentException("Service not found: " + id);
        }
        serviceConfigRepo.setActive(id, active);
    }

    // ====================================================
    // Private helper methods
    // ====================================================

    private void enrichConfig(ServiceConfig config) {
        Long id = config.getId();
        ServiceType type = config.getServiceType();

        switch (type) {
            case SQL -> config.setDbServiceConfig(dbService.getConfigById(id));
            case REST -> config.setRestServiceConfig(
                    restService.getConfigById(id).orElse(new RestServiceConfig()));
            case MAIL -> config.setMailServiceConfig(
                    mailService.getConfigById(id));
            case DOCUMENT -> config.setDocumentServiceConfig(
                    docRepository.findById(id).orElse(new DocumentServiceConfig()));
            default -> {
                // no-op or throw exception
            }
        }
    }

    private void saveDetailConfig(ServiceConfig config) {
        Long id = config.getId();
        ServiceType type = config.getServiceType();

        switch (type) {
            case SQL -> {
                if (config.getDbServiceConfig() != null) {
                    dbService.save(config.getDbServiceConfig());
                }
            }
            case REST -> {
                if (config.getRestServiceConfig() != null) {
                    restService.saveConfig(config.getRestServiceConfig(), id);
                }
            }
            case MAIL -> {
                if (config.getMailServiceConfig() != null) {
                    mailService.saveConfig(config.getMailServiceConfig());
                }
            }
            case DOCUMENT -> {
                if (config.getDocumentServiceConfig() != null) {
                    docRepository.save(config.getDocumentServiceConfig());
                }
            }
            default -> {
                // không lưu gì
            }
        }
    }
}
