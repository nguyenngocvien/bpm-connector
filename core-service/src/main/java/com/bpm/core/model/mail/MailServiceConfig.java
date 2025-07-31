package com.bpm.core.model.mail;

import java.time.LocalDateTime;

public class MailServiceConfig {

    private Long configId;

    private String smtpServer;
    private Integer smtpPort;
    private String username;
    private String password;
    private Boolean useTls;

    private String mailFrom;
    private String mailTo;
    private String mailCc;
    private String mailBcc;

    private String subjectTemplate;
    private String bodyTemplate;

    private String headers;

    private Integer retryCount;
    private Integer retryBackoffMs;
    private Integer timeoutMs;

    private Boolean attachmentsEnabled;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Private constructor: Builder only
    private MailServiceConfig(Builder builder) {
        this.configId = builder.configId;
        this.smtpServer = builder.smtpServer;
        this.smtpPort = builder.smtpPort;
        this.username = builder.username;
        this.password = builder.password;
        this.useTls = builder.useTls;

        this.mailFrom = builder.mailFrom;
        this.mailTo = builder.mailTo;
        this.mailCc = builder.mailCc;
        this.mailBcc = builder.mailBcc;

        this.subjectTemplate = builder.subjectTemplate;
        this.bodyTemplate = builder.bodyTemplate;

        this.headers = builder.headers;

        this.retryCount = builder.retryCount;
        this.retryBackoffMs = builder.retryBackoffMs;
        this.timeoutMs = builder.timeoutMs;

        this.attachmentsEnabled = builder.attachmentsEnabled;

        this.active = builder.active;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // --- Static Builder class
    public static class Builder {
        private Long configId;

        private String smtpServer;
        private Integer smtpPort;
        private String username;
        private String password;
        private Boolean useTls = true;

        private String mailFrom;
        private String mailTo;
        private String mailCc;
        private String mailBcc;

        private String subjectTemplate;
        private String bodyTemplate;

        private String headers;

        private Integer retryCount = 0;
        private Integer retryBackoffMs = 1000;
        private Integer timeoutMs = 5000;

        private Boolean attachmentsEnabled = false;

        private Boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder configId(Long configId) {
            this.configId = configId;
            return this;
        }

        public Builder smtpServer(String smtpServer) {
            this.smtpServer = smtpServer;
            return this;
        }

        public Builder smtpPort(Integer smtpPort) {
            this.smtpPort = smtpPort;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder useTls(Boolean useTls) {
            this.useTls = useTls;
            return this;
        }

        public Builder mailFrom(String mailFrom) {
            this.mailFrom = mailFrom;
            return this;
        }

        public Builder mailTo(String mailTo) {
            this.mailTo = mailTo;
            return this;
        }

        public Builder mailCc(String mailCc) {
            this.mailCc = mailCc;
            return this;
        }

        public Builder mailBcc(String mailBcc) {
            this.mailBcc = mailBcc;
            return this;
        }

        public Builder subjectTemplate(String subjectTemplate) {
            this.subjectTemplate = subjectTemplate;
            return this;
        }

        public Builder bodyTemplate(String bodyTemplate) {
            this.bodyTemplate = bodyTemplate;
            return this;
        }

        public Builder headers(String headers) {
            this.headers = headers;
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

        public Builder attachmentsEnabled(Boolean attachmentsEnabled) {
            this.attachmentsEnabled = attachmentsEnabled;
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

        public MailServiceConfig build() {
            return new MailServiceConfig(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // --- Getters (optionally setters if needed)
    public Long getConfigId() {
        return configId;
    }

    public String getSmtpServer() {
        return smtpServer;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getUseTls() {
        return useTls;
    }

    public String getMailFrom() {
        return mailFrom;
    }

    public String getMailTo() {
        return mailTo;
    }

    public String getMailCc() {
        return mailCc;
    }

    public String getMailBcc() {
        return mailBcc;
    }

    public String getSubjectTemplate() {
        return subjectTemplate;
    }

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public String getHeaders() {
        return headers;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public Integer getRetryBackoffMs() {
        return retryBackoffMs;
    }

    public Integer getTimeoutMs() {
        return timeoutMs;
    }

    public Boolean getAttachmentsEnabled() {
        return attachmentsEnabled;
    }

    public Boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
