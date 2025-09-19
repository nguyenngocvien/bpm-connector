package com.bpm.core.rest.infrastructure;

import com.bpm.core.rest.domain.NameValuePair;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.server.domain.Server;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RestUrlBuilder {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String buildResolvedUrl(RestServiceConfig config, Server server) {
        String baseUrl = (server.isHttps() ? "https://" : "http://")
                + server.getIp() + ":" + server.getPort();

        String fullPath = config.getPath();

        List<NameValuePair> pathParams = parseParams(config.getPathParams());

        if (pathParams != null && !pathParams.isEmpty()) {
            Map<String, String> pathVars = pathParams.stream()
                    .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
            return UriComponentsBuilder.fromUriString(baseUrl + fullPath)
                    .buildAndExpand(pathVars)
                    .toUriString();
        }

        return baseUrl + fullPath;
    }

    private static List<NameValuePair> parseParams(String json) {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(json, new TypeReference<List<NameValuePair>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON params: " + json, e);
        }
    }
}
