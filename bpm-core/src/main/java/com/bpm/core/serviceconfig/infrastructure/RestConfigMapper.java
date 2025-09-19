package com.bpm.core.serviceconfig.infrastructure;

import java.util.List;

import com.bpm.core.rest.domain.NameValuePair;
import com.bpm.core.rest.domain.RestServiceConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestConfigMapper {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void deserializeLists(RestServiceConfig config) {
        try {
            if (config.getHeaders() != null) {
                config.setHeaderList(
                    mapper.readValue(config.getHeaders(), new TypeReference<List<NameValuePair>>() {})
                );
            }
            if (config.getQueryParams() != null) {
                config.setQueryParamList(
                    mapper.readValue(config.getQueryParams(), new TypeReference<List<NameValuePair>>() {})
                );
            }
            if (config.getPathParams() != null) {
                config.setPathParamList(
                    mapper.readValue(config.getPathParams(), new TypeReference<List<NameValuePair>>() {})
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize RestServiceConfig JSON fields", e);
        }
    }
}
