package com.bpm.core.serviceconfig.service;

import com.bpm.core.datasource.domain.DataSourceConfig;
import com.bpm.core.datasource.domain.DbServiceConfig;
import com.bpm.core.datasource.repository.DataSourceRepository;
import com.bpm.core.datasource.repository.DbServiceRepository;
import com.bpm.core.document.domain.FileServiceConfig;
import com.bpm.core.document.repository.FileServiceRepository;
import com.bpm.core.mail.domain.MailServiceConfig;
import com.bpm.core.mail.repository.MailServiceRepository;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.repository.RestServiceRepository;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.repository.ServiceConfigRepository;

import java.util.List;

public class ServiceConfigService {

    private final ServiceConfigRepository serviceConfigRepository;
    private final DataSourceRepository dataSourceRepository;
    private final DbServiceRepository dbServiceRepository;
    private final RestServiceRepository restServiceRepository;
    private final MailServiceRepository mailServiceRepository;
    private final FileServiceRepository fileServiceRepository;

    public ServiceConfigService(ServiceConfigRepository serviceConfigRepository,
    							DataSourceRepository dataSourceRepository,
    							DbServiceRepository dbServiceRepository,
    							RestServiceRepository restServiceRepository,
    							MailServiceRepository mailServiceRepository,
    							FileServiceRepository fileServiceRepository) {
    	
        this.serviceConfigRepository = serviceConfigRepository;
        this.dataSourceRepository = dataSourceRepository;
        this.dbServiceRepository = dbServiceRepository;
        this.restServiceRepository = restServiceRepository;
        this.mailServiceRepository = mailServiceRepository;
        this.fileServiceRepository = fileServiceRepository;
    }

    public List<ServiceConfig> findAll(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return serviceConfigRepository.searchByKeyword(keyword);
        }
        return serviceConfigRepository.findAll();
    }

    public ServiceConfig findById(Long id) {
        ServiceConfig config = serviceConfigRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + id));

        config.setDbServiceConfig(
        		dbServiceRepository.findById(id).orElse(new DbServiceConfig())
        );
        config.setRestServiceConfig(
                restServiceRepository.findById(id).orElse(new RestServiceConfig())
        );
        config.setMailServiceConfig(
                mailServiceRepository.findById(id).orElse(new MailServiceConfig())
        );
        config.setFileServiceConfig(
        		fileServiceRepository.findById(id).orElse(new FileServiceConfig())
        );
        return config;
    }
    
    public ServiceConfig findByCode(String serviceCode) {
        ServiceConfig config = serviceConfigRepository.findByCode(serviceCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Code: " + serviceCode));

        config.setDbServiceConfig(
        		dbServiceRepository.findById(config.getId()).orElse(new DbServiceConfig())
        );
        config.setRestServiceConfig(
                restServiceRepository.findById(config.getId()).orElse(new RestServiceConfig())
        );
        config.setMailServiceConfig(
                mailServiceRepository.findById(config.getId()).orElse(new MailServiceConfig())
        );
        config.setFileServiceConfig(
        		fileServiceRepository.findById(config.getId()).orElse(new FileServiceConfig())
        );
        return config;
    }

    public List<DataSourceConfig> getAllDataSources() {
        return dataSourceRepository.findAll();
    }

    public void save(ServiceConfig config) {
        serviceConfigRepository.save(config);

        if ("DB".equals(config.getServiceType().name()) && config.getDbServiceConfig() != null) {
            dbServiceRepository.save(config.getDbServiceConfig(), config.getId());
        }
        else if ("REST".equals(config.getServiceType().name()) && config.getRestServiceConfig() != null) {
            restServiceRepository.save(config.getRestServiceConfig(), config.getId());
        }
    }

    public void delete(Long id) {
        if (!serviceConfigRepository.existsById(id)) {
            throw new IllegalArgumentException("Service not found: " + id);
        }
        serviceConfigRepository.deleteById(id);
    }

    public void toggleLog(Long id, boolean enabled) {
        if (!serviceConfigRepository.existsById(id)) {
            throw new IllegalArgumentException("Service not found: " + id);
        }
        serviceConfigRepository.enableLog(id, enabled);
    }

    public void toggleActive(Long id, boolean active) {
        if (!serviceConfigRepository.existsById(id)) {
            throw new IllegalArgumentException("Service not found: " + id);
        }
        serviceConfigRepository.setActive(id, active);
    }
}
