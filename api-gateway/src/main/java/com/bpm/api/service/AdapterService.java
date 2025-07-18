package com.bpm.api.service;

import com.bpm.core.service.ServiceConfigLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdapterService {

    private final ServiceConfigLoader configLoader;
    private final ObjectMapper objectMapper;

    private final WebClient webClient = WebClient.builder().build();

    public String invoke(String serviceCode, Map<String, Object> input) {
        LoadedServiceConfi config = configLoader.loadConfig(serviceCode);

        // 1. Map payload theo config
        Map<String, Object> mappedPayload = mapPayload(input, config.payloadMapping());

        // 2. Chuẩn bị headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        config.headers().forEach(headers::set);

        // 3. Gửi request
        try {
            log.info("Invoking [{}] {} with payload: {}", config.httpMethod(), config.targetUrl(), mappedPayload);

            ResponseEntity<String> response = webClient
                    .method(HttpMethod.valueOf(config.httpMethod()))
                    .uri(config.targetUrl())
                    .headers(http -> http.addAll(headers))
                    .bodyValue(mappedPayload)
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            return response != null ? response.getBody() : null;
        } catch (Exception ex) {
            log.error("Failed to invoke external service", ex);
            throw new RuntimeException("External call failed: " + ex.getMessage(), ex);
        }
    }

    private Map<String, Object> mapPayload(Map<String, Object> input, Map<String, String> payloadMapping) {
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, String> entry : payloadMapping.entrySet()) {
            String targetField = entry.getKey();
            String sourcePath = entry.getValue();

            Object value = extractFromPath(input, sourcePath.replace("input.", ""));
            result.put(targetField, value);
        }

        return result;
    }

    private Object extractFromPath(Map<String, Object> input, String path) {
        String[] keys = path.split("\\.");
        Object current = input;

        for (String key : keys) {
            if (current instanceof Map<?, ?> map) {
                current = map.get(key);
            } else {
                return null;
            }
        }

        return current;
    }
}
