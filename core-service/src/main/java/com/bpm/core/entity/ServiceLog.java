package com.bpm.core.entity;

import java.time.LocalDateTime;

public class ServiceLog {
    private String serviceCode;
    private String requestData;
    private String mappedRequest;
    private String responseData;
    private int statusCode;
    private int durationMs;
    private LocalDateTime createdAt;
    
    // Constructors
    public ServiceLog() {
        this.createdAt = LocalDateTime.now();
    }
    
    public ServiceLog(String serviceCode, String requestData, String mappedRequest,
            String responseData, int statusCode, int durationMs) {
		this.serviceCode = serviceCode;
		this.requestData = requestData;
		this.mappedRequest = mappedRequest;
		this.responseData = responseData;
		this.statusCode = statusCode;
		this.durationMs = durationMs;
		this.createdAt = LocalDateTime.now();
	}
    
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getRequestData() {
		return requestData;
	}
	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}
	public String getMappedRequest() {
		return mappedRequest;
	}
	public void setMappedRequest(String mappedRequest) {
		this.mappedRequest = mappedRequest;
	}
	public String getResponseData() {
		return responseData;
	}
	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public int getDurationMs() {
		return durationMs;
	}
	public void setDurationMs(int durationMs) {
		this.durationMs = durationMs;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}

