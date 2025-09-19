package com.bpm.core.document.service;

import com.bpm.core.document.domain.DocumentTemplate;
import com.bpm.core.document.repository.DocumentTemplateRepository;

public class TemplateRepositoryService {

    private DocumentTemplateRepository templateRepository;

    public DocumentTemplate getTemplateById(Long templateId) {
        return templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found with id: " + templateId));
    }

    public DocumentTemplate getTemplateByName(String name) {
        return templateRepository.findByName(name);
    }

    public DocumentTemplate saveTemplate(DocumentTemplate template) {
        return templateRepository.save(template);
    }
}
