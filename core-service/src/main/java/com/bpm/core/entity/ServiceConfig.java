package com.bpm.core.entity;

import java.time.LocalDateTime;

public class ServiceConfig {
    private Long id;
    private String serviceCode;
    private String targetUrl;
    private String httpMethod;
    private String headers;
    private String payloadMapping;
    private Boolean active;
    private Boolean logEnabled;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getHeaders() {
		return headers;
	}
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	public String getPayloadMapping() {
		return payloadMapping;
	}
	public void setPayloadMapping(String payloadMapping) {
		this.payloadMapping = payloadMapping;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Boolean getLogEnabled() {
        return logEnabled;
    }
    public void setLogEnabled(Boolean logEnabled) {
        this.logEnabled = logEnabled;
    }
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
