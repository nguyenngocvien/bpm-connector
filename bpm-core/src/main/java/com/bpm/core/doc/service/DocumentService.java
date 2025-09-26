package com.bpm.core.doc.service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;

import com.bpm.core.cmis.helper.CmisHelper;
import com.bpm.core.cmis.service.CmisSessionService;
import com.bpm.core.doc.domain.DocumentResponse;
import com.bpm.core.doc.domain.DocumentServiceConfig;
import com.bpm.core.doc.domain.DocumentTemplate;
import com.bpm.core.doc.infrastructure.ExcelHelper;
import com.bpm.core.doc.infrastructure.WordHelper;
import com.bpm.core.doc.repository.DocumentTemplateRepository;

public class DocumentService {
	
    private CmisSessionService cmisService;
    private DocumentTemplateRepository templateRepo;

    public DocumentResponse generateFile(DocumentServiceConfig docConfig, Map<String, Object> params) throws IOException {
        DocumentTemplate template = templateRepo.findById(docConfig.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        CmisHelper cmisHelper = cmisService.getOrConnectById(docConfig.getServerId());
        
        byte[] content;
        String mimeType;
        if (template.getType().equalsIgnoreCase("DOCX") || template.getType().equalsIgnoreCase("DOC")) {
            content = WordHelper.generateDocument(template.getName(), "/export", params);
            mimeType = ".docx";
        } else if (template.getType().equalsIgnoreCase("XLSX")) {
            content = ExcelHelper.generateDocument(template.getName(), "/export", params);
            mimeType = ".xlsx";
        } else {
            throw new RuntimeException("Unsupported template type");
        }

        String fileName = template.getName().replaceAll("\\.[^.]+$", "") + "_" + System.currentTimeMillis() + mimeType;
        Document doc = cmisHelper.uploadDocument("Gendoc_Export", fileName, content, mimeType);
        
        String id = doc.getId();

        String base64 = Base64.getEncoder().encodeToString(content);

        return new DocumentResponse(fileName, mimeType, id, base64);
    }
}
