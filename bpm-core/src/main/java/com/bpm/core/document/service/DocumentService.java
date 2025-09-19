package com.bpm.core.document.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.service.AuthRepositoryService;
import com.bpm.core.document.domain.DocumentServiceConfig;
import com.bpm.core.document.domain.DocumentResponse;
import com.bpm.core.document.domain.DocumentTemplate;
import com.bpm.core.document.infrastructure.CmisService;
import com.bpm.core.document.infrastructure.TemplateService;
import com.bpm.core.document.repository.DocumentTemplateRepository;
import com.bpm.core.server.domain.Server;
import com.bpm.core.server.service.ServerService;

public class DocumentService {

    private TemplateService templateService;
    private CmisService cmisService;
    private DocumentTemplateRepository templateRepo;
    private ServerService serverService;
    private AuthRepositoryService authService;

    public DocumentResponse generateFile(DocumentServiceConfig docConfig, Map<String, Object> params) throws IOException {
        DocumentTemplate template = templateRepo.findById(docConfig.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        Server server = serverService.getServerById(docConfig.getServerId());
        		
        AuthConfig auth = authService.getAuthConfigById(docConfig.getAuthId())
                .orElseThrow(() -> new RuntimeException("Auth not found"));
        
        byte[] content;
        String ext;
        if (template.getType().equalsIgnoreCase("DOCX") || template.getType().equalsIgnoreCase("DOC")) {
            content = templateService.generateWordDocument(template.getName(), params);
            ext = ".docx";
        } else if (template.getType().equalsIgnoreCase("XLSX")) {
            content = templateService.generateExcelDocument(template.getName(), params);
            ext = ".xlsx";
        } else {
            throw new RuntimeException("Unsupported template type");
        }

        // 5. Upload CMIS
        String fileName = template.getName().replaceAll("\\.[^.]+$", "") + "_" + System.currentTimeMillis() + ext;
        String url = cmisService.uploadDocument(server, auth, fileName, content);

        // 6. Encode base64
        String base64 = Base64.getEncoder().encodeToString(content);

        return new DocumentResponse(fileName, ext, url, base64);
    }
}
