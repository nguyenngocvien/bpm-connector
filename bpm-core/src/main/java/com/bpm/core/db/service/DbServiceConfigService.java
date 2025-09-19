package com.bpm.core.db.service;

import com.bpm.core.db.domain.DbServiceConfig;
import com.bpm.core.db.domain.DbParamConfig;
import com.bpm.core.db.domain.DbOutputMapping;
import com.bpm.core.db.repository.DbServiceConfigRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

public class DbServiceConfigService {

    private final DbServiceConfigRepository repository;
    private final ObjectMapper objectMapper;
    
    public DbServiceConfigService(DbServiceConfigRepository repository, ObjectMapper objectMapper) {
		this.repository = repository;
		this.objectMapper = objectMapper;
	}

    // Save or update
    public DbServiceConfig save(DbServiceConfig config) {
        try {
            if (config.getParamList() != null) {
                config.setInputParams(objectMapper.writeValueAsString(config.getParamList()));
            }
            if (config.getOutputMappingList() != null) {
                config.setOutputMapping(objectMapper.writeValueAsString(config.getOutputMappingList()));
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize param/output mapping", e);
        }
        return repository.save(config);
    }

    // Find by id
    public DbServiceConfig getConfigById(Long id) {
    	return repository.findById(id)
        		.orElseThrow(() -> new IllegalArgumentException("DbServiceConfig not found with ID: " + id));
    }

    // Find all enabled
    public List<DbServiceConfig> findAllEnabled() {
        List<DbServiceConfig> list = repository.findByEnabledTrue();
        list.forEach(this::deserializeLists);
        return list;
    }

    // Delete
    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private void deserializeLists(DbServiceConfig config) {
        try {
            if (config.getInputParams() != null) {
                List<DbParamConfig> paramList = objectMapper.readValue(
                        config.getInputParams(),
                        new TypeReference<List<DbParamConfig>>() {}
                );
                config.setParamList(paramList);
            } else {
                config.setParamList(Collections.emptyList());
            }

            if (config.getOutputMapping() != null) {
                List<DbOutputMapping> outputList = objectMapper.readValue(
                        config.getOutputMapping(),
                        new TypeReference<List<DbOutputMapping>>() {}
                );
                config.setOutputMappingList(outputList);
            } else {
                config.setOutputMappingList(Collections.emptyList());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize param/output mapping", e);
        }
    }
}
