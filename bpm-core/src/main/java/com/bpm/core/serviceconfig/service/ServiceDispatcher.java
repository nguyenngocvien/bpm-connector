package com.bpm.core.serviceconfig.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.bpm.core.common.response.Response;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.domain.ServiceType;
import com.bpm.core.serviceconfig.interfaces.ServiceInvoker;

public class ServiceDispatcher {
    private final ServiceConfigRepositoryService configService;
    private final Map<ServiceType, ServiceInvoker> invokerMap;

    public ServiceDispatcher(ServiceConfigRepositoryService configService, List<ServiceInvoker> invokers) {
        this.configService = configService;
        this.invokerMap = invokers.stream()
                .collect(Collectors.toMap(ServiceInvoker::getServiceType, Function.identity()));
    }

    public Response execute(Long serviceId, String params) {
        ServiceConfig config = configService.findById(serviceId);
        return execute(config, params);
    }

    public Response execute(String serviceCode, String params) {
        ServiceConfig config = configService.findByCode(serviceCode);
        return execute(config, params);
    }

    private Response execute(ServiceConfig config, String params) {
        ServiceInvoker invoker = invokerMap.get(config.getServiceType());
        if (invoker == null) {
            throw new UnsupportedOperationException("Unsupported service type: " + config.getServiceType());
        }
        return invoker.execute(config, params);
    }
}
