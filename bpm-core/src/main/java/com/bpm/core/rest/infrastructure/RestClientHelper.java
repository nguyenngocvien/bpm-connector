package com.bpm.core.rest.infrastructure;

import com.bpm.core.auth.cache.AuthServiceCache;
import com.bpm.core.common.util.JsonUtil;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.rest.service.RestServiceConfigService;
import com.bpm.core.server.domain.Server;
import com.bpm.core.server.service.ServerRepositoryService;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Map;

@RequiredArgsConstructor
public class RestClientHelper {
    private final ServerRepositoryService serverService;
    private final RestServiceConfigService restService;
    private final AuthServiceCache authCache;
    private final RestRequestMapper requestMapper;
    private final RestResponseMapper responseMapper;

    public String mapRequest(ServiceConfig config, String params) {
        RestServiceConfig restConfig = restService.getActiveConfigById(config.getId());
        Map<String, Object> inputParams = JsonUtil.toObjectMap(params);
        return requestMapper.processRequestMapping(restConfig.getRequestMappingScript(), inputParams);
    }

    public String invoke(ServiceConfig config, String mappedRequest) {
        RestServiceConfig restConfig = restService.getActiveConfigById(config.getId());
        Server server = serverService.getServerById(restConfig.getServerId());

        String resolvedUrl = RestUrlBuilder.buildResolvedUrl(restConfig, server);

        WebClient.Builder builder = WebClient.builder().baseUrl(resolvedUrl);

        // Apply Auth if exists
        if (restConfig.getAuthId() != null) {
            authCache.getAuthById(restConfig.getAuthId())
                    .ifPresent(auth -> WebClientAuthUtil.applyAuth(builder, auth));
        }

        WebClient webClient = builder.build();

        WebClient.RequestBodySpec requestSpec = webClient.method(HttpMethod.valueOf(restConfig.getHttpMethod()))
                .uri(uriBuilder -> {
                    UriComponentsBuilder uriComp = UriComponentsBuilder.fromHttpUrl(resolvedUrl);
                    if (restConfig.getQueryParamList() != null) {
                        restConfig.getQueryParamList().forEach(param ->
                                uriComp.queryParam(param.getName(), param.getValue()));
                    }
                    return uriComp.build().toUri();
                })
                .header(HttpHeaders.CONTENT_TYPE, restConfig.getContentType());

        // Apply custom headers
        if (restConfig.getHeaderList() != null) {
            restConfig.getHeaderList().forEach(header ->
                    requestSpec.header(header.getName(), header.getValue()));
        }

        ClientResponse clientResponse = requestSpec.bodyValue(mappedRequest)
                .exchangeToMono(Mono::just)
                .timeout(Duration.ofMillis(restConfig.getTimeoutMs() != null ? restConfig.getTimeoutMs() : 3000))
                .block();

        String responseString = clientResponse.bodyToMono(String.class).block();

        // map response nếu có script
        Object mappedResponse = responseMapper.processResponseMapping(restConfig.getResponseMappingScript(), responseString);
        return JsonUtil.toString(mappedResponse);
    }
}
