package com.bpm.core.rest.service;

import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.repository.RestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestService {

    private final RestRepository restRepository;

    public RestService(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public List<RestServiceConfig> getAllConfigs() {
        return restRepository.findAll();
    }

    public Optional<RestServiceConfig> getConfigById(Long id) {
        return restRepository.findById(id);
    }

    public void saveConfig(RestServiceConfig config, Long serviceId) {
        restRepository.save(config, serviceId);
    }

    public boolean deleteConfig(Long id) {
        return restRepository.deleteById(id) > 0;
    }
}
