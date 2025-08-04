package com.bpm.core.model.db;

import java.time.LocalDateTime;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceConfig {

    private Integer id;
    private String name;
    private String description;

    private String jdbcUrl;
    private String username;
    private String password;

    private String driverClassName;

    @Default
    private Integer maxPoolSize = 1; //Pool keep 1 connection

    @Default
    private Integer minIdle = 0;

    @Default
    private Integer connectionTimeoutMs = 3000; // 3s timeout when get connection

    @Default
    private Integer idleTimeoutMs = 600000;

    @Default
    private Integer maxLifetimeMs = 1800000;

    @Default
    private Boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public HikariConfig toHikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(this.jdbcUrl);
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setDriverClassName(this.driverClassName);

        if (this.maxPoolSize != null) config.setMaximumPoolSize(this.maxPoolSize);
        if (this.minIdle != null) config.setMinimumIdle(this.minIdle);
        if (this.connectionTimeoutMs != null) config.setConnectionTimeout(this.connectionTimeoutMs);
        if (this.idleTimeoutMs != null) config.setIdleTimeout(this.idleTimeoutMs);
        if (this.maxLifetimeMs != null) config.setMaxLifetime(this.maxLifetimeMs);
        
        config.setValidationTimeout(2000);

        config.setPoolName("ds-" + this.name);

        return config;
    }

    public DataSource toDataSource() {
        return new HikariDataSource(this.toHikariConfig());
    }
}
