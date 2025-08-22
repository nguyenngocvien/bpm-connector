package com.bpm.api.controller.document;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.fncmis.CreateDocumentRequest;
import com.bpm.core.repository.CmisRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping(ROUTES.DOCUMENT)
public class DocumentController {

    private final CmisRepository repository;

    public DocumentController(CmisRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody CreateDocumentRequest req) {
        return ResponseEntity.ok(repository.createDocument(req));
    }
}