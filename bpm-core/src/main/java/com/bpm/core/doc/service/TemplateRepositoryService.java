package com.bpm.core.doc.service;

import java.util.List;

import com.bpm.core.doc.domain.DocumentTemplate;
import com.bpm.core.doc.repository.DocumentTemplateRepository;

public class TemplateRepositoryService {

    private DocumentTemplateRepository templateRepository;
    
    public TemplateRepositoryService(DocumentTemplateRepository templateRepository) {
		this.templateRepository = templateRepository;
	}
    
    public List<DocumentTemplate> getAllTemplates() {
        return templateRepository.findAll();
    }

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
    
    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }
}
