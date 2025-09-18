package com.bpm.core.db.service;

import com.bpm.core.db.domain.DbServiceConfig;
import com.bpm.core.db.repository.DbServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbService {

    private final DbServiceRepository dbServiceRepository;

    public DbService(DbServiceRepository dbServiceRepository) {
        this.dbServiceRepository = dbServiceRepository;
    }

    public List<DbServiceConfig> getAllConfigs() {
        return dbServiceRepository.findAll();
    }

    public DbServiceConfig getConfigById(Long id) {
    	DbServiceConfig config = dbServiceRepository.findById(id) 
    			.orElseThrow(() -> new RuntimeException("DB config not found for ID: " + id));
        return config;
    }

    public void saveConfig(DbServiceConfig config, Long serviceId) {
        dbServiceRepository.save(config, serviceId);
    }

    public boolean deleteConfig(Long id) {
        return dbServiceRepository.deleteById(id) > 0;
    }
}
