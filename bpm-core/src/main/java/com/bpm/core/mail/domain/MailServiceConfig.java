package com.bpm.core.mail.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.bpm.core.serviceconfig.domain.ServiceConfig;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cfg_svc_mail")
public class MailServiceConfig {

    @Id
    @Column(name = "id")
    private Long id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private ServiceConfig serviceConfig;

    @Column(name = "server_id", nullable = false)
    private Long serverId;

    @Column(name = "auth_id")
    private Long authId;

    @Column(name = "default_mail_from", nullable = false)
    private String defaultMailFrom;

    @Column(name = "default_mail_to", columnDefinition = "TEXT")
    private String defaultMailTo;

    @Column(name = "default_mail_cc", columnDefinition = "TEXT")
    private String defaultMailCc;

    @Column(name = "default_mail_bcc", columnDefinition = "TEXT")
    private String defaultMailBcc;

    @Column(name = "subject_template", columnDefinition = "TEXT")
    private String subjectTemplate;

    @Column(name = "body_template", columnDefinition = "TEXT")
    private String bodyTemplate;

    @Column(columnDefinition = "TEXT")
    private String headers;

    @Builder.Default
    @Column(name = "retry_count")
    private Integer retryCount = 0;

    @Builder.Default
    @Column(name = "retry_backoff_ms")
    private Integer retryBackoffMs = 1000;

    @Builder.Default
    @Column(name = "timeout_ms")
    private Integer timeoutMs = 5000;

    @Builder.Default
    @Column(name = "attachments_enabled")
    private Boolean attachmentsEnabled = false;

    @Builder.Default
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
}
