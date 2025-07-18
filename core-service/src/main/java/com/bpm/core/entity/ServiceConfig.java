package com.bpm.core.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_code", nullable = false, unique = true)
    private String serviceCode;

    @Column(name = "target_url", nullable = false)
    private String targetUrl;

    @Column(name = "http_method", nullable = false)
    private String httpMethod;

    @Column(columnDefinition = "TEXT")
    private String headers; // JSON string

    @Column(name = "payload_mapping", columnDefinition = "TEXT")
    private String payloadMapping; // JSON string

    private Boolean active;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
