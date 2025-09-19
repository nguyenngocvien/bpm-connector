package com.bpm.core.db.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.Builder.Default;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_datasources")
public class DataSourceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "url", nullable = false)
    private String jdbcUrl;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "driver_class_name", nullable = false)
    private String driverClassName;

    @Default
    @Column(name = "max_pool_size")
    private Integer maxPoolSize = 1;

    @Default
    @Column(name = "min_idle")
    private Integer minIdle = 0;

    @Default
    @Column(name = "connection_timeout_ms")
    private Integer connectionTimeoutMs = 3000;

    @Default
    @Column(name = "idle_timeout_ms")
    private Integer idleTimeoutMs = 600000;

    @Default
    @Column(name = "max_lifetime_ms")
    private Integer maxLifetimeMs = 1800000;

    @Default
    private Boolean active = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public com.zaxxer.hikari.HikariConfig toHikariConfig() {
        com.zaxxer.hikari.HikariConfig config = new com.zaxxer.hikari.HikariConfig();
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

    public javax.sql.DataSource toDataSource() {
        return new com.zaxxer.hikari.HikariDataSource(this.toHikariConfig());
    }
}
