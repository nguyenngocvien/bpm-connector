package com.bpm.core.model.rest;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestServiceConfig {

    private Long id;
    private String targetUrl;
    
    @Builder.Default
    private String httpMethod = "GET";
    
    @Builder.Default
    private String contentType = "application/json";

    @Builder.Default
    private Integer timeoutMs = 3000;

    @Builder.Default
    private Integer retryCount = 0;

    @Builder.Default
    private Integer retryBackoffMs = 1000;

    private String payloadTemplate;
    private String responseMapping;

    private Integer authId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<RestHeader> headers;
    private List<RestQueryParam> queryParams;
    private List<RestPathParam> pathParams;
}
