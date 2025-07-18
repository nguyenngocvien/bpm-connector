package com.bpm.core.service;

import com.bpm.core.entity.ServiceConfig;
import com.bpm.core.repository.ServiceConfigRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ServiceConfigLoader {

    private final ServiceConfigRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServiceConfig loadConfig(String serviceCode) {
        ServiceConfig entity = repository.findByServiceCodeAndActiveTrue(serviceCode)
                .orElseThrow(() -> new RuntimeException("Service config not found or inactive: " + serviceCode));

        try {
            Map<String, String> headers = objectMapper.readValue(entity.getHeaders(), new TypeReference<>() {});
            Map<String, String> payloadMapping = objectMapper.readValue(entity.getPayloadMapping(), new TypeReference<>() {});

            return new LoadedServiceConfig(
                    entity.getTargetUrl(),
                    entity.getHttpMethod(),
                    headers,
                    payloadMapping
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse config JSON", e);
        }
    }

    public record LoadedServiceConfig(
            String targetUrl,
            String httpMethod,
            Map<String, String> headers,
            Map<String, String> payloadMapping
    ) {}
}
