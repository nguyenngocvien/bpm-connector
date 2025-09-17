package com.bpm.core.datasource.domain;

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
    private Long dbSourceId;
    private String sqlStatement;
    private SqlType sqlType; // QUERY, UPDATE, PROC
    
    private String inputParams;
    private String outputMapping;

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
