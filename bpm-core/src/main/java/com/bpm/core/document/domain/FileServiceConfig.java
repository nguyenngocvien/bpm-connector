package com.bpm.core.document.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileServiceConfig {

    private Long configId;
    private String filePath;
    private String fileAction;

    private String fileFormat;
    private String fileTemplate;

    private String fileNamePattern;
    private String fileEncoding = "UTF-8";  // Default

    private String extraConfig;

    private Integer retryCount = 0;         // Default
    private Integer retryBackoffMs = 1000;  // Default
    private Integer timeoutMs = 5000;       // Default

    private Boolean active = true;          // Default
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
