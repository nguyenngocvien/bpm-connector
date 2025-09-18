package com.bpm.core.serviceconfig.domain;

import java.time.LocalDateTime;

import com.bpm.core.db.domain.DbServiceConfig;
import com.bpm.core.document.domain.FileServiceConfig;
import com.bpm.core.mail.domain.MailServiceConfig;
import com.bpm.core.rest.domain.RestServiceConfig;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ServiceConfig {

    private Long id;
    private String serviceCode;
    private String serviceName;
    private ServiceType serviceType; // REST, SOAP, DB, MAIL, FILE
    private String serviceDescription;

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
