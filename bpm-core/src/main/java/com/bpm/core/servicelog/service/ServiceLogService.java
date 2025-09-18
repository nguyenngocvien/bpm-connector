package com.bpm.core.servicelog.service;

import com.bpm.core.servicelog.domain.ServiceLog;
import com.bpm.core.servicelog.repository.ServiceLogRepository;

public class ServiceLogService {

    private final ServiceLogRepository repository;

    public ServiceLogService(ServiceLogRepository repository) {
        this.repository = repository;
    }

    public Long createLog(ServiceLog log) {
        if (log.getCreatedAt() == null) {
            throw new IllegalArgumentException("createdAt must not be null");
        }
        return repository.insertLog(log);
    }
}
