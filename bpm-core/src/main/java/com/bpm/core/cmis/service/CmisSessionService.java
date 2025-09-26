package com.bpm.core.cmis.service;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.service.AuthRepositoryService;
import com.bpm.core.cmis.domain.CmisSessionConfig;
import com.bpm.core.cmis.helper.CmisHelper;
import com.bpm.core.cmis.repository.CmisSessionConfigRepository;

public class CmisSessionService {

    private final CmisSessionConfigRepository repository;
    private final AuthRepositoryService authService;
    private final CmisHelper cmisHelper;
    
    public CmisSessionService(
    		CmisSessionConfigRepository repository,
    		AuthRepositoryService authService,
    		CmisHelper cmisHelper) {
    	
    	this.repository = repository;
    	this.authService = authService;
    	this.cmisHelper = cmisHelper;
		
	}
    
    /**
     * Connect CMIS by name
     */
    public void connectById(Long id) {
        CmisSessionConfig config = repository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("CMIS CONFIG NOT FOUND: " + id));
        
        AuthConfig authConfig = authService.getAuthConfigById(config.getAuthId());

        cmisHelper.connect(
                config.getAtompubUrl(),
                authConfig.getUsername(),
                authConfig.getPassword(),
                config.getRepositoryId()
        );
    }

    /**
     * Connect CMIS by name
     */
    public void connectByName(String name) {
        CmisSessionConfig config = repository.findByNameAndActiveTrue(name)
                .orElseThrow(() -> new RuntimeException("CMIS CONFIG NOT FOUND: " + name));
        
        AuthConfig authConfig = authService.getAuthConfigById(config.getAuthId());

        cmisHelper.connect(
                config.getAtompubUrl(),
                authConfig.getUsername(),
                authConfig.getPassword(),
                config.getRepositoryId()
        );
    }

    /**
     * Connect by first active configuration
     */
    public void connectDefault() {
        CmisSessionConfig config = repository.findFirstByActiveTrue()
                .orElseThrow(() -> new RuntimeException("NO ACTIVE CMIS CONFIG FOUND"));

        AuthConfig authConfig = authService.getAuthConfigById(config.getAuthId());
        
        cmisHelper.connect(
                config.getAtompubUrl(),
                authConfig.getUsername(),
                authConfig.getPassword(),
                config.getRepositoryId()
        );
    }

    /**
     * get CmisHelper
     */
    public CmisHelper getHelper() {
        return cmisHelper;
    }
}
