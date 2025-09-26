package com.bpm.core.cmis.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.service.AuthRepositoryService;
import com.bpm.core.cmis.domain.CmisSessionConfig;
import com.bpm.core.cmis.helper.CmisHelper;
import com.bpm.core.cmis.repository.CmisSessionConfigRepository;

public class CmisSessionService {

    private final CmisSessionConfigRepository repository;
    private final AuthRepositoryService authService;

    private final Map<String, CmisHelper> sessionCache = new ConcurrentHashMap<>();
    
    public CmisSessionService(CmisSessionConfigRepository repository, AuthRepositoryService authService) {
	    this.repository = repository;
	    this.authService = authService;
	}

    public CmisHelper getOrConnectByName(String configName) {
        return sessionCache.computeIfAbsent("name:" + configName, k -> {
            CmisSessionConfig config = repository.findByNameAndActiveTrue(configName)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy config: " + configName));

            AuthConfig authConfig = authService.getAuthConfigById(config.getAuthId());

            CmisHelper helper = new CmisHelper();
            helper.connect(
                    config.getAtompubUrl(),
                    authConfig.getUsername(),
                    authConfig.getPassword(),
                    config.getRepositoryId()
            );
            return helper;
        });
    }

    public CmisHelper getOrConnectById(Long serverId) {
        return sessionCache.computeIfAbsent("id:" + serverId, k -> {
            CmisSessionConfig config = repository.findById(serverId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy config id=" + serverId));

            AuthConfig authConfig = authService.getAuthConfigById(config.getAuthId());

            CmisHelper helper = new CmisHelper();
            helper.connect(
                    config.getAtompubUrl(),
                    authConfig.getUsername(),
                    authConfig.getPassword(),
                    config.getRepositoryId()
            );
            return helper;
        });
    }

    public void clearCache(String key) {
        sessionCache.remove(key);
    }

    public void clearAllCache() {
        sessionCache.clear();
    }
}

