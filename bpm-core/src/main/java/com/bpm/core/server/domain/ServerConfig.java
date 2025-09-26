package com.bpm.core.server.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cfg_cmis_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;   // Server Name

    @Column(nullable = false, length = 20)
    private String type;   // REST, MAIL, DB...

    @Column(nullable = false, length = 50)
    private String ip;     // IP Address

    @Column(nullable = false)
    private Integer port;  // Port

    @Column(nullable = false)
    private boolean https; // HTTPS enabled

    @Column(name = "atom_pub_url")
    private String atomPubUrl;

    @Column(name = "repo_id")
    private String repoId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
