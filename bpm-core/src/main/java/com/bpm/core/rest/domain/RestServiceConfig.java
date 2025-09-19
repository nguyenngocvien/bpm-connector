package com.bpm.core.rest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cfg_service_rest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestServiceConfig {

    @Id
    private Long id; // tham chiếu core_services(id)

    @Column(name = "server_id")
    private Long serverId;

    @Column(name = "auth_id")
    private Long authId;

    @Column(nullable = false)
    private String path;

    @Builder.Default
    @Column(name = "http_method", length = 10, nullable = false)
    private String httpMethod = "GET";

    @Builder.Default
    @Column(name = "content_type", length = 50)
    private String contentType = "application/json";

    @Builder.Default
    @Column(name = "timeout_ms")
    private Integer timeoutMs = 3000;

    @Builder.Default
    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Builder.Default
    @Column(name = "retry_backoff_ms")
    private Integer retryBackoffMs = 1000;

    @Column(name = "request_mapping_script", columnDefinition = "TEXT")
    private String requestMappingScript;

    @Column(name = "response_mapping_script", columnDefinition = "TEXT")
    private String responseMappingScript;

    @Column(columnDefinition = "TEXT")
    private String headers; // JSONB

    @Column(name = "query_params", columnDefinition = "TEXT")
    private String queryParams; // JSONB

    @Column(name = "path_params", columnDefinition = "TEXT")
    private String pathParams; // JSONB

    @Builder.Default
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
