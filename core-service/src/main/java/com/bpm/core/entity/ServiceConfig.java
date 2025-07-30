package com.bpm.core.entity;

import java.time.LocalDateTime;

public class ServiceConfig {
    private Long id;
    private String serviceCode;
    private String serviceType;

    // REST / SOAP / MAIL / FILE config
    private String targetUrl;
    private String httpMethod;
    private String headers;
    private String payloadTemplate;

    // DB config
    private String dbDatasource;
    private String sqlStatement;
    private String sqlType;

    // common
    private Boolean logEnabled;
    private Boolean active;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getTargetUrl() { return targetUrl; }
    public void setTargetUrl(String targetUrl) { this.targetUrl = targetUrl; }

    public String getHttpMethod() { return httpMethod; }
    public void setHttpMethod(String httpMethod) { this.httpMethod = httpMethod; }

    public String getHeaders() { return headers; }
    public void setHeaders(String headers) { this.headers = headers; }

    public String getPayloadTemplate() { return payloadTemplate; }
    public void setPayloadTemplate(String payloadTemplate) { this.payloadTemplate = payloadTemplate; }

    public String getDbDatasource() { return dbDatasource; }
    public void setDbDatasource(String dbDatasource) { this.dbDatasource = dbDatasource; }

    public String getSqlStatement() { return sqlStatement; }
    public void setSqlStatement(String sqlStatement) { this.sqlStatement = sqlStatement; }

    public String getSqlType() { return sqlType; }
    public void setSqlType(String sqlType) { this.sqlType = sqlType; }

    public Boolean getLogEnabled() { return logEnabled; }
    public void setLogEnabled(Boolean logEnabled) { this.logEnabled = logEnabled; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "ServiceConfig{" +
                "id=" + id +
                ", serviceCode='" + serviceCode + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", targetUrl='" + targetUrl + '\'' +
                ", httpMethod='" + httpMethod + '\'' +
                ", headers='" + headers + '\'' +
                ", payloadTemplate='" + payloadTemplate + '\'' +
                ", dbDatasource='" + dbDatasource + '\'' +
                ", sqlStatement='" + sqlStatement + '\'' +
                ", sqlType='" + sqlType + '\'' +
                ", logEnabled=" + logEnabled +
                ", active=" + active +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
