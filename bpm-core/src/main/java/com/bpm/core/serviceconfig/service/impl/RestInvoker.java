package com.bpm.core.serviceconfig.service.impl;

import com.bpm.core.auth.cache.AuthServiceCache;
import com.bpm.core.common.response.Response;
import com.bpm.core.common.util.JsonUtil;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.infrastructure.RestRequestMapper;
import com.bpm.core.rest.infrastructure.RestResponseMapper;
import com.bpm.core.rest.infrastructure.RestUrlBuilder;
import com.bpm.core.rest.infrastructure.ServiceLogHelper;
import com.bpm.core.rest.infrastructure.WebClientAuthUtil;
import com.bpm.core.rest.service.RestServiceConfigService;
import com.bpm.core.server.domain.Server;
import com.bpm.core.server.service.ServerRepositoryService;
import com.bpm.core.servicelog.domain.ServiceLog;
import com.bpm.core.servicelog.service.ServiceLogService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.*;

public class RestInvoker {

    private final ServerRepositoryService serverService;
    private final RestServiceConfigService restService;
    private final ServiceLogService logService;
    private final AuthServiceCache authCache;

    private final RestRequestMapper requestMapper;
    private final RestResponseMapper responseMapper;

    public RestInvoker(ServerRepositoryService serverService, RestServiceConfigService restService,
                       ServiceLogService logService, AuthServiceCache authCache,
                       RestRequestMapper requestMapper,
                       RestResponseMapper responseMapper) {
        this.serverService = serverService;
        this.restService = restService;
        this.logService = logService;
        this.authCache = authCache;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    public Response<Object> invoke(Long serviceId, String params) {
    	
        RestServiceConfig config = restService.getActiveConfigById(serviceId);

        Server server = serverService.getServerById(config.getServerId());
        
        String resolvedUrl = RestUrlBuilder.buildResolvedUrl(config, server);

        WebClient.Builder builder = WebClient.builder().baseUrl(resolvedUrl);

        // --- apply auth ---
        if (config.getAuthId() != null) {
            authCache.getAuthById(config.getAuthId())
                     .ifPresent(auth -> WebClientAuthUtil.applyAuth(builder, auth));
        }

        WebClient webClient = builder.build();

        WebClient.RequestBodySpec request = webClient.method(HttpMethod.valueOf(config.getHttpMethod()))
                .uri(uriBuilder -> {
                    UriComponentsBuilder uriComp = UriComponentsBuilder.fromHttpUrl(resolvedUrl);
                    if (config.getQueryParamList() != null) {
                        config.getQueryParamList().forEach(param ->
                                uriComp.queryParam(param.getName(), param.getValue()));
                    }
                    return uriComp.build().toUri();
                })
                .header(HttpHeaders.CONTENT_TYPE, config.getContentType());

        // --- apply custom headers ---
        if (config.getHeaderList() != null) {
            config.getHeaderList().forEach(header ->
                    request.header(header.getName(), header.getValue()));
        }

        // --- build request body ---
        Map<String,Object> inputParams = JsonUtil.toObjectMap(params);
        String bodyStr = requestMapper.processRequestMapping(config.getRequestMappingScript(), inputParams);

        long startTime = System.currentTimeMillis();
        Long logId = null;
        int statusCode = 200;
        String responseString = null;

        try {
            responseString = request.bodyValue(bodyStr)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofMillis(
                            config.getTimeoutMs() != null ? config.getTimeoutMs() : 3000
                    ))
                    .block();

            Object mappedResponse = responseMapper.processResponseMapping(config.getResponseMappingScript(), responseString);

            // --- logging ---
            if (Boolean.TRUE.equals(config.getServiceConfig().getLogEnabled())) {
                ServiceLog log = ServiceLogHelper.buildLog(config.getServiceConfig(), inputParams, bodyStr, responseString, statusCode, startTime);
                logId = saveLogSafe(log);
            }

            return Response.success(mappedResponse);

        } catch (Exception ex) {
            statusCode = 500;
            String errMsg = ex.getMessage();

            if (Boolean.TRUE.equals(config.getServiceConfig().getLogEnabled())) {
                ServiceLog log = ServiceLogHelper.buildLog(config.getServiceConfig(), inputParams, bodyStr, errMsg, statusCode, startTime);
                logId = saveLogSafe(log);
            }

            return Response.error(errMsg + (logId != null ? " [Log_ID: " + logId + "]" : ""));
        }
    }

    private Long saveLogSafe(ServiceLog log) {
        try {
            return logService.createLog(log);
        } catch (Exception e) {
            System.err.println("Log insert failed: " + e.getMessage());
            return null;
        }
    }
}

