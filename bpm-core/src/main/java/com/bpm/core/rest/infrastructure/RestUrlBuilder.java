package com.bpm.core.rest.infrastructure;

import com.bpm.core.rest.domain.NameValuePair;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.bpm.core.server.domain.Server;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.stream.Collectors;

public class RestUrlBuilder {

    public static String buildResolvedUrl(RestServiceConfig config, Server server) {
        String baseUrl = (server.isHttps() ? "https://" : "http://")
                + server.getIp() + ":" + server.getPort();

        String fullPath = config.getPath();

        if (config.getPathParamList() != null && !config.getPathParamList().isEmpty()) {
            Map<String, String> pathVars = config.getPathParamList().stream()
                    .collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue));
            return UriComponentsBuilder.fromUriString(baseUrl + fullPath)
                    .buildAndExpand(pathVars)
                    .toUriString();
        }

        return baseUrl + fullPath;
    }
}
