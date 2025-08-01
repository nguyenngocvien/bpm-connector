package com.bpm.core.model.service;

import java.time.LocalDateTime;

import com.bpm.core.model.db.DbServiceConfig;
import com.bpm.core.model.file.FileServiceConfig;
import com.bpm.core.model.mail.MailServiceConfig;
import com.bpm.core.model.rest.RestServiceConfig;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.AllArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfig {

    private Long id;

    @NonNull
    private String serviceCode;
    
    private String serviceName;
    private String serviceDescription;

    @NonNull
    private ServiceType serviceType; // REST, SOAP, DB, MAIL, FILE

    @Builder.Default
    private Boolean logEnabled = true;

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Integer version = 1;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private DbServiceConfig dbServiceConfig;
    private RestServiceConfig restServiceConfig;
    private MailServiceConfig mailServiceConfig;
    private FileServiceConfig fileServiceConfig;
}
