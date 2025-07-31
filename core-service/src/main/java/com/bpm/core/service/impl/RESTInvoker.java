package com.bpm.core.service.impl;

import com.bpm.core.cache.AuthServiceCache;
import com.bpm.core.model.log.ServiceLog;
import com.bpm.core.model.response.Response;
import com.bpm.core.model.service.ServiceConfig;
import com.bpm.core.model.rest.RestServiceConfig;
import com.bpm.core.repository.RestServiceRepository;
import com.bpm.core.repository.ServiceLogRepository;
import com.bpm.core.service.ServiceInvoker;
import com.bpm.core.util.RestHeaderUtil;
import com.bpm.core.util.WebClientAuthUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;

@Service("restInvoker")
public class RESTInvoker implements ServiceInvoker {

    private final RestServiceRepository repository;
    private final ServiceLogRepository logRepository;
    private final AuthServiceCache authCache;
    private final ObjectMapper objectMapper;

    public RESTInvoker(RestServiceRepository repository, ServiceLogRepository logRepository, AuthServiceCache authCache) {
        this.repository = repository;
        this.logRepository = logRepository;
        this.authCache = authCache;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Response<Object> invoke(ServiceConfig serviceConfig, Map<String, Object> inputParams) {
        Optional<RestServiceConfig> optionalConfig = repository.findById(serviceConfig.getId());
        if (!optionalConfig.isPresent()) {
            return Response.error("REST config not found for ID: " + serviceConfig.getId());
        }

        RestServiceConfig config = optionalConfig.get();

        WebClient.Builder builder = WebClient.builder().baseUrl(config.getTargetUrl());
        if (config.getAuthId() != null) {
            authCache.getAuthById(config.getAuthId())
                    .ifPresent(auth -> WebClientAuthUtil.applyAuth(builder, auth));
        }

        WebClient webClient = builder.build();
        WebClient.RequestBodySpec request = webClient.method(HttpMethod.valueOf(config.getHttpMethod()))
                .uri(uriBuilder -> {
                    UriComponentsBuilder uriComp = UriComponentsBuilder.fromHttpUrl(config.getTargetUrl());
                    if (config.getQueryParams() != null) {
                        config.getQueryParams().forEach(q -> uriComp.queryParam(q.getParamName(), q.getParamValue()));
                    }
                    return uriComp.build().toUri();
                })
                .header(HttpHeaders.CONTENT_TYPE, config.getContentType());

        // Headers
        Map<String, String> headers = RestHeaderUtil.toHeaderMap(config.getHeaders());
        headers.forEach(request::header);

        // Body
        String bodyStr = processPayloadTemplate(config.getPayloadTemplate(), inputParams);

        long start = System.currentTimeMillis();
        String responseString = null;
        int statusCode = 200;
        Long logId = null;

        try {
            Mono<String> responseMono = request.bodyValue(bodyStr)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(config.getTimeoutMs() != null ? config.getTimeoutMs() : 3000));

            responseString = responseMono.block();

            Object mappedResponse = mapResponse(responseString, config.getResponseMapping());
            
            // Log nếu cần
            if (Boolean.TRUE.equals(serviceConfig.getLogEnabled())) {
                ServiceLog log = new ServiceLog(
                        serviceConfig.getServiceCode(),
                        inputParams.toString(),
                        bodyStr,
                        responseString,
                        statusCode,
                        (int) (System.currentTimeMillis() - start)
                );
                try {
                    logId = logRepository.insertLog(log);
                } catch (Exception ignore) {}
            }

            return Response.success(mappedResponse);

        } catch (Exception ex) {
            statusCode = 500;
            String errMsg = ex.getMessage();
            if (Boolean.TRUE.equals(serviceConfig.getLogEnabled())) {
                ServiceLog log = new ServiceLog(
                        serviceConfig.getServiceCode(),
                        inputParams.toString(),
                        bodyStr,
                        errMsg,
                        statusCode,
                        (int) (System.currentTimeMillis() - start)
                );
                try {
                    logId = logRepository.insertLog(log);
                } catch (Exception ignore) {}
            }
            return Response.error(errMsg + (logId != null ? " [Log_ID: " + logId + "]" : ""));
        }
    }

    private String processPayloadTemplate(String template, Map<String, Object> params) {
        if (template == null || template.isEmpty()) return "";
        String processed = template;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            processed = processed.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }
        return processed;
    }

    /**
     * Map JSON response to desired output based on responseMapping
     * Supports: JSONPath (e.g., $.data.items)
     */
    private Object mapResponse(String responseStr, String mappingRule) {
        if (mappingRule == null || mappingRule.isEmpty()) {
            return responseStr;
        }

        try {
            JsonNode root = objectMapper.readTree(responseStr);

            if (mappingRule.startsWith("$.") || mappingRule.startsWith("$[")) {
                // JSONPath mapping
                Object extracted = JsonPath.read(responseStr, mappingRule);
                return extracted;
            }

            // Future: support JMESPath, SpEL...
            return root;
        } catch (Exception ex) {
            return responseStr;  // fallback raw
        }
    }
}
