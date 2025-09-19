package com.bpm.core.db.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import com.bpm.core.serviceconfig.domain.ServiceConfig;

import lombok.*;
import lombok.Builder.Default;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cfg_service_db")
public class DbServiceConfig {

    @Id
    private Long id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private ServiceConfig serviceConfig;

    @Column(name = "ds_id", nullable = false)
    private Long dbSourceId;

    @Column(name = "sql_statement", columnDefinition = "TEXT", nullable = false)
    private String sqlStatement;

    @Enumerated(EnumType.STRING)
    @Column(name = "sql_type", nullable = false)
    private SqlType sqlType;

    @Column(name = "input_params", columnDefinition = "TEXT")
    private String inputParams;

    @Column(name = "output_mapping", columnDefinition = "TEXT")
    private String outputMapping;

    @Default
    @Column(name = "timeout_ms")
    private Integer timeoutMs = 3000;

    @Default
    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Default
    @Column(name = "retry_backoff_ms")
    private Integer retryBackoffMs = 1000;

    @Default
    @Column(name = "transactional")
    private Boolean transactional = false;

    @Default
    @Column(name = "fetch_size")
    private Integer fetchSize = 100;

    @Default
    @Column(name = "result_type")
    private String resultType = "LIST";

    @Default
    private Boolean enabled = true;

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

    @Transient
    private List<DbParamConfig> paramList;

    @Transient
    private List<DbOutputMapping> outputMappingList;
}
