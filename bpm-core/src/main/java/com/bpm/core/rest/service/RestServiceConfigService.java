package com.bpm.core.rest.service;

import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.repository.RestServiceConfigRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestServiceConfigService {

    private final RestServiceConfigRepository restRepository;

    public RestServiceConfigService(RestServiceConfigRepository restRepository) {
        this.restRepository = restRepository;
    }

    public List<RestServiceConfig> getAllConfigs() {
        return restRepository.findAll();
    }

    public Optional<RestServiceConfig> getConfigById(Long id) {
        return restRepository.findById(id);
    }

    public void saveConfig(RestServiceConfig config, Long serviceId) {
    	config.setId(serviceId);
    	restRepository.save(config);
    }

    public void deleteConfig(Long id) {
    	restRepository.deleteById(id);
    }
}
