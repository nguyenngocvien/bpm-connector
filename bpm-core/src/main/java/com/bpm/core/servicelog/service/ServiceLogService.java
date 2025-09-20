package com.bpm.core.servicelog.service;

import java.util.List;

import com.bpm.core.servicelog.domain.ServiceLog;
import com.bpm.core.servicelog.repository.ServiceLogRepository;

public class ServiceLogService {

    private final ServiceLogRepository repository;

    public ServiceLogService(ServiceLogRepository repository) {
        this.repository = repository;
    }

    public Long createLog(ServiceLog log) {
    	return repository.save(log).getId();
    }
    
    public List<ServiceLog> getLogsByServiceCode(String serviceCode) {
        return repository.findByServiceCodeOrderByCreatedAtDesc(serviceCode);
    }

    public List<ServiceLog> getLatestLogs() {
        return repository.findTop10ByOrderByCreatedAtDesc();
    }
}
