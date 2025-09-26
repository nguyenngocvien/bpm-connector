package com.bpm.core.cmis.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cmis_session_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CmisSessionConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "atompub_url")
    private String atompubUrl;

    @Column(name = "repository_id")
    private String repositoryId;
    
    @Column(name = "auth_id")
    private Long authId;
    
    @Column(name = "binding_type")
    private String bindingType;

    private Boolean active;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
