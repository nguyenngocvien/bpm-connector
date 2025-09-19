package com.bpm.core.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Server {
    private Long id;
    private String name;   // Server Name
    private ServerType type;   // API, MAIL, FILE
    private String ip;     // IP Address
    private Integer port;  // Port
    private boolean https; // HTTPS enabled
    private String atomPubUrl;
    private String repoId;
}
