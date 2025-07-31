package com.bpm.core.model.db;

import java.time.LocalDateTime;
import java.util.List;

public class DbServiceConfig {

    private Long id;
    private String dbDatasource;
    private String sqlStatement;
    private String sqlType; // QUERY, UPDATE, PROC

    private String inputParams;      // JSON or comma-separated
    private String outputMapping;    // JSON or another mapping

    private Integer timeoutMs;
    private Integer retryCount;
    private Integer retryBackoffMs;

    private Boolean transactional;
    private Integer fetchSize;

    private String resultType;       // LIST, SINGLE, NONE
    private Boolean enabled;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Optional: mapped params & output map
    private List<DbParamConfig> paramList;
    private List<DbOutputMapping> outputMappingList;

    private DbServiceConfig(Builder builder) {
        this.id = builder.id;
        this.dbDatasource = builder.dbDatasource;
        this.sqlStatement = builder.sqlStatement;
        this.sqlType = builder.sqlType;

        this.inputParams = builder.inputParams;
        this.outputMapping = builder.outputMapping;

        this.timeoutMs = builder.timeoutMs;
        this.retryCount = builder.retryCount;
        this.retryBackoffMs = builder.retryBackoffMs;

        this.transactional = builder.transactional;
        this.fetchSize = builder.fetchSize;

        this.resultType = builder.resultType;
        this.enabled = builder.enabled;

        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;

        this.paramList = builder.paramList;
        this.outputMappingList = builder.outputMappingList;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String dbDatasource;
        private String sqlStatement;
        private String sqlType;

        private String inputParams;
        private String outputMapping;

        private Integer timeoutMs = 3000;
        private Integer retryCount = 0;
        private Integer retryBackoffMs = 1000;

        private Boolean transactional = false;
        private Integer fetchSize = 100;

        private String resultType = "LIST";
        private Boolean enabled = true;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private List<DbParamConfig> paramList;
        private List<DbOutputMapping> outputMappingList;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder dbDatasource(String dbDatasource) { this.dbDatasource = dbDatasource; return this; }
        public Builder sqlStatement(String sqlStatement) { this.sqlStatement = sqlStatement; return this; }
        public Builder sqlType(String sqlType) { this.sqlType = sqlType; return this; }

        public Builder inputParams(String inputParams) { this.inputParams = inputParams; return this; }
        public Builder outputMapping(String outputMapping) { this.outputMapping = outputMapping; return this; }

        public Builder timeoutMs(Integer timeoutMs) { this.timeoutMs = timeoutMs; return this; }
        public Builder retryCount(Integer retryCount) { this.retryCount = retryCount; return this; }
        public Builder retryBackoffMs(Integer retryBackoffMs) { this.retryBackoffMs = retryBackoffMs; return this; }

        public Builder transactional(Boolean transactional) { this.transactional = transactional; return this; }
        public Builder fetchSize(Integer fetchSize) { this.fetchSize = fetchSize; return this; }

        public Builder resultType(String resultType) { this.resultType = resultType; return this; }
        public Builder enabled(Boolean enabled) { this.enabled = enabled; return this; }

        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }

        public Builder paramList(List<DbParamConfig> paramList) { this.paramList = paramList; return this; }
        public Builder outputMappingList(List<DbOutputMapping> outputMappingList) { this.outputMappingList = outputMappingList; return this; }

        public DbServiceConfig build() {
            return new DbServiceConfig(this);
        }
    }

    // --- Getters (optionally setters)
    public Long getId() { return id; }
    public String getDbDatasource() { return dbDatasource; }
    public String getSqlStatement() { return sqlStatement; }
    public String getSqlType() { return sqlType; }
    public String getInputParams() { return inputParams; }
    public String getOutputMapping() { return outputMapping; }
    public Integer getTimeoutMs() { return timeoutMs; }
    public Integer getRetryCount() { return retryCount; }
    public Integer getRetryBackoffMs() { return retryBackoffMs; }
    public Boolean getTransactional() { return transactional; }
    public Integer getFetchSize() { return fetchSize; }
    public String getResultType() { return resultType; }
    public Boolean getEnabled() { return enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<DbParamConfig> getParamList() { return paramList; }
    public List<DbOutputMapping> getOutputMappingList() { return outputMappingList; }

	public void setParamList(List<DbParamConfig> paramList) {
		this.paramList = paramList;
	}

	public void setOutputMappingList(List<DbOutputMapping> outputMappingList) {
		this.outputMappingList = outputMappingList;
	}
}
