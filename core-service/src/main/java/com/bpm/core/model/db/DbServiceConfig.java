package com.bpm.core.model.db;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DbServiceConfig {

    private Long id;
    private String dbDatasource;
    private String sqlStatement;
    private String sqlType; // QUERY, UPDATE, PROC

    private String inputParams;      // JSON or comma-separated
    private String outputMapping;    // JSON or another mapping

    @Default
    private Integer timeoutMs = 3000;

    @Default
    private Integer retryCount = 0;

    @Default
    private Integer retryBackoffMs = 1000;

    @Default
    private Boolean transactional = false;

    @Default
    private Integer fetchSize = 100;

    @Default
    private String resultType = "LIST";

    @Default
    private Boolean enabled = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<DbParamConfig> paramList;
    private List<DbOutputMapping> outputMappingList;
}
