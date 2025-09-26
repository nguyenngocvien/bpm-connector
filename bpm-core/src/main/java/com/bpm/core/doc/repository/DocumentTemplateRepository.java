package com.bpm.core.doc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bpm.core.doc.domain.DocumentTemplate;

public interface DocumentTemplateRepository extends JpaRepository<DocumentTemplate, Long> {

    DocumentTemplate findByName(String name);
}
