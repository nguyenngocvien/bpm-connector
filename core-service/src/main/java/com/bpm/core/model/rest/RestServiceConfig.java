package com.bpm.core.model.rest;

import java.time.LocalDateTime;
import java.util.List;

public class RestServiceConfig {

    private Long id;
    private String targetUrl;
    private String httpMethod;
    private String contentType;

    private Integer timeoutMs;
    private Integer retryCount;
    private Integer retryBackoffMs;

    private String payloadTemplate;
    private String responseMapping;

    private Integer authId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<RestHeader> headers;
    private List<RestQueryParam> queryParams;
    private List<RestPathParam> pathParams;

    private RestServiceConfig(Builder builder) {
        this.id = builder.id;
        this.targetUrl = builder.targetUrl;
        this.httpMethod = builder.httpMethod;
        this.contentType = builder.contentType;
        this.timeoutMs = builder.timeoutMs;
        this.retryCount = builder.retryCount;
        this.retryBackoffMs = builder.retryBackoffMs;
        this.payloadTemplate = builder.payloadTemplate;
        this.responseMapping = builder.responseMapping;
        this.authId = builder.authId;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.headers = builder.headers;
        this.queryParams = builder.queryParams;
        this.pathParams = builder.pathParams;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String targetUrl;
        private String httpMethod = "GET";
        private String contentType = "application/json";

        private Integer timeoutMs = 3000;
        private Integer retryCount = 0;
        private Integer retryBackoffMs = 1000;

        private String payloadTemplate;
        private String responseMapping;

        private Integer authId;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        private List<RestHeader> headers;
        private List<RestQueryParam> queryParams;
        private List<RestPathParam> pathParams;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder targetUrl(String targetUrl) {
            this.targetUrl = targetUrl;
            return this;
        }

        public Builder httpMethod(String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public Builder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder timeoutMs(Integer timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder retryCount(Integer retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public Builder retryBackoffMs(Integer retryBackoffMs) {
            this.retryBackoffMs = retryBackoffMs;
            return this;
        }

        public Builder payloadTemplate(String payloadTemplate) {
            this.payloadTemplate = payloadTemplate;
            return this;
        }

        public Builder responseMapping(String responseMapping) {
            this.responseMapping = responseMapping;
            return this;
        }

        public Builder authId(Integer authId) {
            this.authId = authId;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder headers(List<RestHeader> headers) {
            this.headers = headers;
            return this;
        }

        public Builder queryParams(List<RestQueryParam> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public Builder pathParams(List<RestPathParam> pathParams) {
            this.pathParams = pathParams;
            return this;
        }

        public RestServiceConfig build() {
            return new RestServiceConfig(this);
        }
    }

    // --- Getters and Setters (if needed)
    public Long getId() { return id; }
    public String getTargetUrl() { return targetUrl; }
    public String getHttpMethod() { return httpMethod; }
    public String getContentType() { return contentType; }
    public Integer getTimeoutMs() { return timeoutMs; }
    public Integer getRetryCount() { return retryCount; }
    public Integer getRetryBackoffMs() { return retryBackoffMs; }
    public String getPayloadTemplate() { return payloadTemplate; }
    public String getResponseMapping() { return responseMapping; }
    public Integer getAuthId() { return authId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public List<RestHeader> getHeaders() { return headers; }
    public List<RestQueryParam> getQueryParams() { return queryParams; }
    public List<RestPathParam> getPathParams() { return pathParams; }

    public void setId(Long id) {
		this.id = id;
	}
    
	public void setHeaders(List<RestHeader> headers) {
		this.headers = headers;
	}

	public void setQueryParams(List<RestQueryParam> queryParams) {
		this.queryParams = queryParams;
	}

	public void setPathParams(List<RestPathParam> pathParams) {
		this.pathParams = pathParams;
	}
}
