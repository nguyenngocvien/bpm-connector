package com.bpm.core.servicelog.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "svc_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_code", nullable = false, length = 50)
    private String serviceCode;

    @Column(name = "request_data", columnDefinition = "TEXT")
    private String requestData;

    @Column(name = "mapped_request", columnDefinition = "TEXT")
    private String mappedRequest;

    @Column(name = "response_data", columnDefinition = "TEXT")
    private String responseData;

    @Column(name = "status_code")
    private int statusCode;

    @Column(name = "duration_ms")
    private int durationMs;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
