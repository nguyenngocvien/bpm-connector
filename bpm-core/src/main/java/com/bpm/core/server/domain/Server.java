package com.bpm.core.server.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cfg_servers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Server {

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
}
