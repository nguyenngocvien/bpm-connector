package com.bpm.core.doc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import com.bpm.core.serviceconfig.domain.ServiceConfig;

import jakarta.persistence.*;

@Entity
@Table(name = "cfg_doc_srvcs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentServiceConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private ServiceConfig serviceConfig;

    @Column(name = "template_id", nullable = false)
    private Long templateId;

    @Column(name = "server_id", nullable = false)
    private Long serverId;

    @Column(name = "auth_id")
    private Long authId;

    @Column(nullable = false)
    private boolean active = true;

    @Column(length = 50)
    private String version;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

