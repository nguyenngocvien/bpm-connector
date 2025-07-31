package com.bpm.core.model.db;

import java.time.LocalDateTime;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceConfig {

    private Integer id;
    private String name;
    private String description;

    private String url;
    private String username;
    private String password;

    private String driverClassName;

    private Integer maxPoolSize;
    private Integer minIdle;
    private Integer connectionTimeoutMs;
    private Integer idleTimeoutMs;
    private Integer maxLifetimeMs;

    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public DataSourceConfig() {
        // initialize default values if needed
        this.maxPoolSize = 10;
        this.minIdle = 2;
        this.connectionTimeoutMs = 30000;
        this.idleTimeoutMs = 600000;
        this.maxLifetimeMs = 1800000;
        this.active = true;
    }

    // --- Private constructor: only Builder can create
    private DataSourceConfig(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.url = builder.url;
        this.username = builder.username;
        this.password = builder.password;
        this.driverClassName = builder.driverClassName;

        this.maxPoolSize = builder.maxPoolSize;
        this.minIdle = builder.minIdle;
        this.connectionTimeoutMs = builder.connectionTimeoutMs;
        this.idleTimeoutMs = builder.idleTimeoutMs;
        this.maxLifetimeMs = builder.maxLifetimeMs;

        this.active = builder.active;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    // --- Static Builder class
    public static class Builder {
        private Integer id;
        private String name;
        private String description;

        private String url;
        private String username;
        private String password;

        private String driverClassName;

        private Integer maxPoolSize = 10;
        private Integer minIdle = 2;
        private Integer connectionTimeoutMs = 30000;
        private Integer idleTimeoutMs = 600000;
        private Integer maxLifetimeMs = 1800000;

        private Boolean active = true;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
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

        public Builder driverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
            return this;
        }

        public Builder maxPoolSize(Integer maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
            return this;
        }

        public Builder minIdle(Integer minIdle) {
            this.minIdle = minIdle;
            return this;
        }

        public Builder connectionTimeoutMs(Integer connectionTimeoutMs) {
            this.connectionTimeoutMs = connectionTimeoutMs;
            return this;
        }

        public Builder idleTimeoutMs(Integer idleTimeoutMs) {
            this.idleTimeoutMs = idleTimeoutMs;
            return this;
        }

        public Builder maxLifetimeMs(Integer maxLifetimeMs) {
            this.maxLifetimeMs = maxLifetimeMs;
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

        public DataSourceConfig build() {
            return new DataSourceConfig(this);
        }
    }

    // --- Static factory for builder
    public static Builder builder() {
        return new Builder();
    }
    
    public HikariConfig toHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.url);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setDriverClassName(this.driverClassName);

        // Optional pool settings
        if (this.maxPoolSize != null) config.setMaximumPoolSize(this.maxPoolSize);
        if (this.minIdle != null) config.setMinimumIdle(this.minIdle);
        if (this.connectionTimeoutMs != null) config.setConnectionTimeout(this.connectionTimeoutMs);
        if (this.idleTimeoutMs != null) config.setIdleTimeout(this.idleTimeoutMs);
        if (this.maxLifetimeMs != null) config.setMaxLifetime(this.maxLifetimeMs);

        // Set pool name for easier monitoring/logging
        config.setPoolName("ds-" + this.name);

        return config;
    }

    public DataSource toDataSource() {
        return new HikariDataSource(this.toHikariConfig());
    }

    // --- Getters only (immutable style)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public Integer getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(Integer maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public Integer getIdleTimeoutMs() {
        return idleTimeoutMs;
    }

    public void setIdleTimeoutMs(Integer idleTimeoutMs) {
        this.idleTimeoutMs = idleTimeoutMs;
    }

    public Integer getMaxLifetimeMs() {
        return maxLifetimeMs;
    }

    public void setMaxLifetimeMs(Integer maxLifetimeMs) {
        this.maxLifetimeMs = maxLifetimeMs;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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
