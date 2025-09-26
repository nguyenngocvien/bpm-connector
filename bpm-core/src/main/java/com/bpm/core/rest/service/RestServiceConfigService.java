package com.bpm.core.rest.service;

import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.repository.RestServiceConfigRepository;
import com.bpm.core.serviceconfig.infrastructure.RestConfigMapper;

import java.util.List;

public class RestServiceConfigService {

    private final RestServiceConfigRepository restRepository;

    public RestServiceConfigService(RestServiceConfigRepository restRepository) {
        this.restRepository = restRepository;
    }

    public List<RestServiceConfig> getAllConfigs() {
        return restRepository.findAll();
    }

    public RestServiceConfig getConfigById(Long id) {
    	RestServiceConfig config = restRepository.findById(id)
    	        .map(cfg -> {
    	            RestConfigMapper.deserializeLists(cfg);
    	            return cfg;
    	        })
    	        .orElseThrow(() -> new IllegalArgumentException("Not found: " + id));
        return config;
    }
    
    public RestServiceConfig getActiveConfigById(Long id) {
    	RestServiceConfig config = restRepository.findByIdAndServiceConfig_ActiveTrue(id)
    	        .map(cfg -> {
    	            RestConfigMapper.deserializeLists(cfg);
    	            return cfg;
    	        })
    	        .orElseThrow(() -> new IllegalArgumentException("Not found: " + id));
        return config;
    }

    public void saveConfig(RestServiceConfig config, Long serviceId) {
    	config.setId(serviceId);
    	restRepository.save(config);
    }

    public void deleteConfig(Long id) {
    	restRepository.deleteById(id);
    }
}
