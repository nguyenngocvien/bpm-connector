package com.bpm.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Store {
	private final AuthRepository authRepository;
	private final ServiceConfigRepository serviceConfigRepository;
    private final DataSourceRepository datasourceRepository;
    private final DbServiceRepository dbServiceRepository;
    private final RestServiceRepository restServiceRepository;
    private final MailServiceRepository mailServiceRepository;
    private final FileServiceRepository fileServiceRepository;

    @Autowired
    public Store(AuthRepository authRepository,
    				ServiceConfigRepository serviceConfigRepository,
    				DataSourceRepository datasourceRepository,
    				DbServiceRepository dbServiceRepository,
    				RestServiceRepository restServiceRepository,
    				MailServiceRepository mailServiceRepository,
    				FileServiceRepository fileServiceRepository) {
    	
    	this.authRepository = authRepository;
        this.serviceConfigRepository = serviceConfigRepository;
        this.datasourceRepository = datasourceRepository;
        this.dbServiceRepository = dbServiceRepository;
        this.restServiceRepository = restServiceRepository;
        this.mailServiceRepository = mailServiceRepository;
        this.fileServiceRepository = fileServiceRepository;
    }
    
    public AuthRepository auths() {
        return authRepository;
    }

    public ServiceConfigRepository serviceConfigs() {
        return serviceConfigRepository;
    }

    public DataSourceRepository datasources() {
        return datasourceRepository;
    }
    
    public DbServiceRepository dbServices() {
        return dbServiceRepository;
    }
    
    public RestServiceRepository restServices() {
        return restServiceRepository;
    }
    
    public MailServiceRepository mailServices() {
        return mailServiceRepository;
    }
    
    public FileServiceRepository fileServices() {
        return fileServiceRepository;
    }
}