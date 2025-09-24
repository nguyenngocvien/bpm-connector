package com.bpm.core.serviceconfig.service.impl;

import java.util.Map;

import com.bpm.core.common.response.Response;
import com.bpm.core.document.domain.DocumentResponse;
import com.bpm.core.document.domain.DocumentServiceConfig;
import com.bpm.core.document.service.DocumentService;
import com.bpm.core.serviceconfig.domain.ServiceConfig;

public class DocGenerater {

    private DocumentService documentService;

    public Object invoke(ServiceConfig serviceConfig, Map<String, Object> params) {

        DocumentServiceConfig docConfig = serviceConfig.getDocumentServiceConfig();
        if (docConfig == null) {
            throw new RuntimeException("DocumentServiceConfig not found for serviceId: " + serviceConfig.getId());
        }

        // 3. Generate file
        try {
        	DocumentResponse document = documentService.generateFile(docConfig, params); 
        	return document;
        } catch (Exception e) {
        	return Response.error("Failed to generate document: " + e.getMessage());
        }
    }
}
