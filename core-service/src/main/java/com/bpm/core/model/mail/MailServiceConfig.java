package com.bpm.core.model.mail;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailServiceConfig {

    private Long configId;

    private String smtpServer;
    private Integer smtpPort;
    private String username;
    private String password;

    @Builder.Default
    private Boolean useTls = true;

    private String mailFrom;
    private String mailTo;
    private String mailCc;
    private String mailBcc;

    private String subjectTemplate;
    private String bodyTemplate;

    private String headers;

    @Builder.Default
    private Integer retryCount = 0;

    @Builder.Default
    private Integer retryBackoffMs = 1000;

    @Builder.Default
    private Integer timeoutMs = 5000;

    @Builder.Default
    private Boolean attachmentsEnabled = false;

    @Builder.Default
    private Boolean active = true;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
