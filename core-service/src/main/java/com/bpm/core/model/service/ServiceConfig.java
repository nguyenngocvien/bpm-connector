package com.bpm.core.model.service;

import java.time.LocalDateTime;

public class ServiceConfig {

    private Long id;

    private String serviceCode;
    private String serviceName;
    private String serviceDescription;

    private String serviceType; // REST, SOAP, DB, MAIL, FILE

    private Boolean logEnabled;
    private Boolean active;
    private Integer version;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public ServiceConfig() {
		// TODO Auto-generated constructor stub
	}

    // --- Private constructor
    private ServiceConfig(Builder builder) {
        this.id = builder.id;
        this.serviceCode = builder.serviceCode;
        this.serviceName = builder.serviceName;
        this.serviceDescription = builder.serviceDescription;
        this.serviceType = builder.serviceType;
        this.logEnabled = builder.logEnabled;
        this.active = builder.active;
        this.version = builder.version;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // --- Static Builder class
    public static class Builder {
        private Long id;
        private String serviceCode;
        private String serviceName;
        private String serviceDescription;
        private String serviceType;
        private Boolean logEnabled = false;
        private Boolean active = true;
        private Integer version = 1;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder serviceCode(String serviceCode) {
            this.serviceCode = serviceCode;
            return this;
        }

        public Builder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public Builder serviceDescription(String serviceDescription) {
            this.serviceDescription = serviceDescription;
            return this;
        }

        public Builder serviceType(String serviceType) {
            this.serviceType = serviceType;
            return this;
        }

        public Builder logEnabled(Boolean logEnabled) {
            this.logEnabled = logEnabled;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
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

        public ServiceConfig build() {
            return new ServiceConfig(this);
        }
    }

    // --- Static factory
    public static Builder builder() {
        return new Builder();
    }

    // --- Getters & Setters
    public Long getId() {
        return id;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getServiceType() {
        return serviceType;
    }

    public Boolean getLogEnabled() {
        return logEnabled;
    }

    public Boolean getActive() {
        return active;
    }

    public Integer getVersion() {
        return version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // --- Optional: Setters nếu cần thay đổi
    public void setId(Long id) {
        this.id = id;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceDescription(String serviceDescription) {
        this.serviceDescription = serviceDescription;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setLogEnabled(Boolean logEnabled) {
        this.logEnabled = logEnabled;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
