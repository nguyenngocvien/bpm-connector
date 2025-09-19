package com.bpm.core.document.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bpm.core.document.domain.DocumentTemplate;

public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Long> {

    DocumentTemplate findByName(String name);
}
