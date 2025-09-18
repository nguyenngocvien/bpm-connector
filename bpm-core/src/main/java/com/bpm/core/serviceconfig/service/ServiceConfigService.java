package com.bpm.core.serviceconfig.service;

import com.bpm.core.db.service.DbService;
import com.bpm.core.document.domain.FileServiceConfig;
import com.bpm.core.document.service.FileService;
import com.bpm.core.mail.domain.MailServiceConfig;
import com.bpm.core.mail.service.MailService;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.service.RestService;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.repository.ServiceConfigRepository;

import java.util.List;

public class ServiceConfigService {

	private final ServiceConfigRepository serviceConfig;
    private final DbService db;
//    private final ServerService server;
    private final RestService rest;
    private final MailService mail;
    private final FileService file;

    public ServiceConfigService(ServiceConfigRepository serviceConfig,
                                DbService db,
//                                ServerService server,
                                RestService rest,
                                MailService mail,
                                FileService file) {
        this.serviceConfig = serviceConfig;
        this.db = db;
//        this.server = server;
        this.rest = rest;
        this.mail = mail;
        this.file = file;
    }

    public List<ServiceConfig> findAll(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return serviceConfig.searchByKeyword(keyword);
        }
        return serviceConfig.findAll();
    }

    public ServiceConfig findById(Long id) {
        ServiceConfig config = serviceConfig.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service ID: " + id));

        config.setDbServiceConfig(db.getConfigById(id));
        
        config.setRestServiceConfig(
                rest.getConfigById(id).orElse(new RestServiceConfig())
        );
        config.setMailServiceConfig(
                mail.getMailConfigById(id).orElse(new MailServiceConfig())
        );
        config.setFileServiceConfig(
        		file.getConfigById(id).orElse(new FileServiceConfig())
        );
        return config;
    }
    
    public ServiceConfig findByCode(String serviceCode) {
        ServiceConfig config = serviceConfig.findByCode(serviceCode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Code: " + serviceCode));

        config.setDbServiceConfig(db.getConfigById(config.getId()));
        
        config.setRestServiceConfig(
        		rest.getConfigById(config.getId()).orElse(new RestServiceConfig())
        );
        config.setMailServiceConfig(
        		mail.getMailConfigById(config.getId()).orElse(new MailServiceConfig())
        );
        config.setFileServiceConfig(
        		file.getConfigById(config.getId()).orElse(new FileServiceConfig())
        );
        return config;
    }

    public void save(ServiceConfig config) {
    	serviceConfig.save(config);

        if ("DB".equals(config.getServiceType().name()) && config.getDbServiceConfig() != null) {
            db.saveConfig(config.getDbServiceConfig(), config.getId());
        }
        else if ("REST".equals(config.getServiceType().name()) && config.getRestServiceConfig() != null) {
            rest.saveConfig(config.getRestServiceConfig(), config.getId());
        }
    }

    public void delete(Long id) {
        if (!serviceConfig.existsById(id)) {
            throw new IllegalArgumentException("Service not found: " + id);
        }
        serviceConfig.deleteById(id);
    }

    public void toggleLog(Long id, boolean enabled) {
        if (!serviceConfig.existsById(id)) {
            throw new IllegalArgumentException("Service not found: " + id);
        }
        serviceConfig.enableLog(id, enabled);
    }

    public void toggleActive(Long id, boolean active) {
        if (!serviceConfig.existsById(id)) {
            throw new IllegalArgumentException("Service not found: " + id);
        }
        serviceConfig.setActive(id, active);
    }
    
}
