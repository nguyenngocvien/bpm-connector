package com.bpm.core.db.service;

import com.bpm.core.db.domain.DataSourceConfig;
import com.bpm.core.db.repository.DataSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DataSourceConfigService {

    private final DataSourceRepository dataSourceRepository;

    public List<DataSourceConfig> getAllDataSources() {
        return dataSourceRepository.findAll();
    }

    public DataSourceConfig getDataSourceById(Long id) {
        return dataSourceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DataSourceConfig not found with ID: " + id));
    }

    public Optional<DataSourceConfig> getDataSourceByName(String name) {
        return dataSourceRepository.findByName(name);
    }

    public DataSourceConfig saveDataSource(DataSourceConfig config) {
        return dataSourceRepository.save(config);
    }

    public boolean deleteDataSource(Long id) {
        if (dataSourceRepository.existsById(id)) {
            dataSourceRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
