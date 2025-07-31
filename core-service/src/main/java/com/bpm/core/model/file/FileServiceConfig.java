package com.bpm.core.model.file;

import java.time.LocalDateTime;

public class FileServiceConfig {

    private Long configId;
    private String filePath;
    private String fileAction;

    private String fileFormat;
    private String fileTemplate;

    private String fileNamePattern;
    private String fileEncoding;

    private String extraConfig;

    private Integer retryCount;
    private Integer retryBackoffMs;
    private Integer timeoutMs;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private FileServiceConfig(Builder builder) {
        this.configId = builder.configId;
        this.filePath = builder.filePath;
        this.fileAction = builder.fileAction;
        this.fileFormat = builder.fileFormat;
        this.fileTemplate = builder.fileTemplate;
        this.fileNamePattern = builder.fileNamePattern;
        this.fileEncoding = builder.fileEncoding;
        this.extraConfig = builder.extraConfig;
        this.retryCount = builder.retryCount;
        this.retryBackoffMs = builder.retryBackoffMs;
        this.timeoutMs = builder.timeoutMs;
        this.active = builder.active;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long configId;
        private String filePath;
        private String fileAction;

        private String fileFormat;
        private String fileTemplate;

        private String fileNamePattern;
        private String fileEncoding = "UTF-8";

        private String extraConfig;

        private Integer retryCount = 0;
        private Integer retryBackoffMs = 1000;
        private Integer timeoutMs = 5000;

        private Boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder configId(Long configId) {
            this.configId = configId;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder fileAction(String fileAction) {
            this.fileAction = fileAction;
            return this;
        }

        public Builder fileFormat(String fileFormat) {
            this.fileFormat = fileFormat;
            return this;
        }

        public Builder fileTemplate(String fileTemplate) {
            this.fileTemplate = fileTemplate;
            return this;
        }

        public Builder fileNamePattern(String fileNamePattern) {
            this.fileNamePattern = fileNamePattern;
            return this;
        }

        public Builder fileEncoding(String fileEncoding) {
            this.fileEncoding = fileEncoding;
            return this;
        }

        public Builder extraConfig(String extraConfig) {
            this.extraConfig = extraConfig;
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

        public Builder timeoutMs(Integer timeoutMs) {
            this.timeoutMs = timeoutMs;
            return this;
        }

        public Builder active(Boolean active) {
            this.active = active;
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

        public FileServiceConfig build() {
            return new FileServiceConfig(this);
        }
    }

    // --- Getters và setters nếu cần
    public Long getConfigId() { return configId; }
    public String getFilePath() { return filePath; }
    public String getFileAction() { return fileAction; }
    public String getFileFormat() { return fileFormat; }
    public String getFileTemplate() { return fileTemplate; }
    public String getFileNamePattern() { return fileNamePattern; }
    public String getFileEncoding() { return fileEncoding; }
    public String getExtraConfig() { return extraConfig; }
    public Integer getRetryCount() { return retryCount; }
    public Integer getRetryBackoffMs() { return retryBackoffMs; }
    public Integer getTimeoutMs() { return timeoutMs; }
    public Boolean getActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Optional: add toString(), equals(), hashCode() as needed
}
