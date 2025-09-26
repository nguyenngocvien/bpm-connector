package com.bpm.core.cmis.service;

import com.bpm.core.cmis.domain.CmisSessionConfig;
import com.bpm.core.cmis.repository.CmisSessionConfigRepository;

import java.util.List;
import java.util.Optional;

public class CmisSessionConfigService {

    private final CmisSessionConfigRepository repository;

    public CmisSessionConfigService(CmisSessionConfigRepository repository) {
        this.repository = repository;
    }

    public List<CmisSessionConfig> getAll() {
        return repository.findAll();
    }

    public Optional<CmisSessionConfig> getById(Long id) {
        return repository.findById(id);
    }

    public CmisSessionConfig save(CmisSessionConfig config) {
        return repository.save(config);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
