package com.bpm.core.serviceconfig.service.impl;

import com.bpm.core.auth.cache.AuthServiceCache;
import com.bpm.core.common.response.Response;
import com.bpm.core.log.domain.ServiceLog;
import com.bpm.core.log.service.ServiceLogService;
import com.bpm.core.rest.domain.NameValuePair;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.infrastructure.WebClientAuthUtil;
import com.bpm.core.rest.service.RestService;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.service.ServiceInvoker;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.JsonPath;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class RESTInvoker implements ServiceInvoker {

    private final RestService restService;
    private final ServiceLogService logService;
    private final AuthServiceCache authCache;
    private final ObjectMapper objectMapper;

    public RESTInvoker(RestService restService, ServiceLogService logService, AuthServiceCache authCache) {
        this.restService = restService;
        this.logService = logService;
        this.authCache = authCache;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Response<Object> invoke(ServiceConfig serviceConfig, Map<String, Object> inputParams) {
        Optional<RestServiceConfig> optionalConfig = restService.getConfigById(serviceConfig.getId());
        if (!optionalConfig.isPresent()) {
            return Response.error("REST config not found for ID: " + serviceConfig.getId());
        }

        RestServiceConfig config = optionalConfig.get();

        String resolvedUrl = buildResolvedUrl(config);
        WebClient.Builder builder = WebClient.builder().baseUrl(resolvedUrl);

        // Apply auth if needed
        if (config.getAuthId() != null) {
            authCache.getAuthById(config.getAuthId())
                     .ifPresent(auth -> WebClientAuthUtil.applyAuth(builder, auth));
        }

        WebClient webClient = builder.build();
        WebClient.RequestBodySpec request = webClient.method(HttpMethod.valueOf(config.getHttpMethod()))
                .uri(uriBuilder -> {
                    UriComponentsBuilder uriComp = UriComponentsBuilder.fromHttpUrl(resolvedUrl);
                    // Add Query Params
                    if (config.getQueryParamList() != null) {
                        config.getQueryParamList().forEach(param -> 
                            uriComp.queryParam(param.getName(), param.getValue()));
                    }
                    return uriComp.build().toUri();
                })
                .header(HttpHeaders.CONTENT_TYPE, config.getContentType());

        // Add Headers
        if (config.getHeaderList() != null) {
            config.getHeaderList().forEach(header -> 
                request.header(header.getName(), header.getValue()));
        }

        String bodyStr = processPayloadTemplate(config.getPayloadTemplate(), inputParams);

        long startTime = System.currentTimeMillis();
        String responseString = null;
        int statusCode = 200;
        Long logId = null;

        try {
            Mono<String> responseMono = request.bodyValue(bodyStr)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(Optional.ofNullable(config.getTimeoutMs()).orElse(3000)));

            responseString = responseMono.block();
            Object mappedResponse = mapResponse(responseString, config.getResponseMapping());

            // Log request/response
            if (Boolean.TRUE.equals(serviceConfig.getLogEnabled())) {
                ServiceLog log = buildLog(serviceConfig, inputParams, bodyStr, responseString, statusCode, startTime);
                logId = saveLogSafe(log);
            }

            return Response.success(mappedResponse);

        } catch (Exception ex) {
            statusCode = 500;
            String errMsg = ex.getMessage();

            if (Boolean.TRUE.equals(serviceConfig.getLogEnabled())) {
                ServiceLog log = buildLog(serviceConfig, inputParams, bodyStr, errMsg, statusCode, startTime);
                logId = saveLogSafe(log);
            }

            return Response.error(errMsg + (logId != null ? " [Log_ID: " + logId + "]" : ""));
        }
    }

    // Resolve URL with path params
    private String buildResolvedUrl(RestServiceConfig config) {
        if (config.getPathParamList() != null && !config.getPathParamList().isEmpty()) {
            Map<String, String> pathVars = config.getPathParamList().stream()
                    .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
            return UriComponentsBuilder.fromUriString(config.getTargetUrl())
                    .buildAndExpand(pathVars)
                    .toUriString();
        }
        return config.getTargetUrl();
    }

    // Template handle payload
    private String processPayloadTemplate(String template, Map<String, Object> params) {
        if (template == null || template.isEmpty()) return "";
        String processed = template;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            processed = processed.replace("${" + entry.getKey() + "}", entry.getValue().toString());
        }
        return processed;
    }

    // Mapping output from JSON
    private Object mapResponse(String responseStr, String mappingRule) {
        if (mappingRule == null || mappingRule.isEmpty()) {
            return responseStr;
        }
        try {
            JsonNode root = objectMapper.readTree(responseStr);
            if (mappingRule.startsWith("$.") || mappingRule.startsWith("$[")) {
                return JsonPath.read(responseStr, mappingRule);
            }
            return root;
        } catch (Exception ex) {
            return responseStr;
        }
    }

    // build log object
    private ServiceLog buildLog(ServiceConfig serviceConfig, Map<String, Object> inputParams,
                                String bodyStr, String responseStr, int statusCode, long startTime) {
        ServiceLog log = new ServiceLog();
        log.setServiceCode(serviceConfig.getServiceCode());
        log.setRequestData(inputParams.toString());
        log.setMappedRequest(bodyStr);
        log.setResponseData(responseStr);
        log.setStatusCode(statusCode);
        log.setDurationMs((int) (System.currentTimeMillis() - startTime));
        return log;
    }

    // write log safe
    private Long saveLogSafe(ServiceLog log) {
        try {
            return logService.createLog(log);
        } catch (Exception e) {
            System.err.println("Log insert failed: " + e.getMessage());
            return null;
        }
    }
}
