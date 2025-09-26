package com.bpm.core.serviceconfig.domain;

import com.bpm.core.db.domain.DbServiceConfig;
import com.bpm.core.doc.domain.DocumentServiceConfig;
import com.bpm.core.mail.domain.MailServiceConfig;
import com.bpm.core.rest.domain.RestServiceConfig;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cfg_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "service_code", nullable = false, unique = true, length = 50)
    private String serviceCode;

    @Column(name = "service_name", nullable = false, length = 100)
    private String serviceName;

    @Column(name = "service_description", columnDefinition = "TEXT")
    private String serviceDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type", nullable = false, length = 20)
    private ServiceType serviceType;

    @Column(name = "log_enabled")
    private Boolean logEnabled = true;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "version")
    private Integer version = 1;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToOne(mappedBy = "serviceConfig", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private DbServiceConfig dbServiceConfig;

    @OneToOne(mappedBy = "serviceConfig", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private RestServiceConfig restServiceConfig;

    @OneToOne(mappedBy = "serviceConfig", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private MailServiceConfig mailServiceConfig;

    @OneToOne(mappedBy = "serviceConfig", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private DocumentServiceConfig documentServiceConfig;


    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
