package com.bpm.core.db.service;

import com.bpm.core.db.domain.DataSourceConfig;
import com.bpm.core.db.repository.DataSourceRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataSourceService {

    private final DataSourceRepository dataSourceRepository;

    public DataSourceService(DataSourceRepository dataSourceRepository) {
        this.dataSourceRepository = dataSourceRepository;
    }

    public List<DataSourceConfig> getAllDataSources() {
        return dataSourceRepository.findAll();
    }

    public DataSourceConfig getDataSourceById(Long id) {
    	DataSourceConfig config = dataSourceRepository.findById(id) 
    			.orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
    	
        return config;
    }

    public Optional<DataSourceConfig> getDataSourceByName(String name) {
        return dataSourceRepository.findByName(name);
    }

    public DataSourceConfig saveDataSource(DataSourceConfig config) {
        int result = dataSourceRepository.save(config);
        if (result > 0) {
            if (config.getId() != null) {
                return dataSourceRepository.findById(config.getId())
                        .orElse(config);
            } else {
                return dataSourceRepository.findByName(config.getName())
                        .orElse(config);
            }
        }
        throw new RuntimeException("Could not save DataSourceConfig");
    }

    public boolean deleteDataSource(Long id) {
        return dataSourceRepository.deleteById(id) > 0;
    }
}
