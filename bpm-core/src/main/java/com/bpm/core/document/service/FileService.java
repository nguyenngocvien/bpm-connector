package com.bpm.core.document.service;

import com.bpm.core.document.domain.FileServiceConfig;
import com.bpm.core.document.repository.FileServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileServiceRepository fileServiceRepository;

    public FileService(FileServiceRepository fileServiceRepository) {
        this.fileServiceRepository = fileServiceRepository;
    }

    public List<FileServiceConfig> getAllConfigs() {
        return fileServiceRepository.findAll();
    }

    public Optional<FileServiceConfig> getConfigById(Long configId) {
        return fileServiceRepository.findById(configId);
    }

    public boolean saveConfig(FileServiceConfig config) {
        return fileServiceRepository.save(config) > 0;
    }

    public boolean deleteConfig(Long configId) {
        return fileServiceRepository.deleteById(configId) > 0;
    }

    public boolean exists(Long configId) {
        return fileServiceRepository.existsById(configId);
    }
}
