package com.bpm.api.modules.document.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.fncmis.CreateDocRequest;
import com.bpm.core.model.fncmis.CreateDocResponse;
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
    public ResponseEntity<CreateDocResponse> create(@Valid @RequestBody CreateDocRequest req) {
        return ResponseEntity.ok(repository.createDocument(req));
    }
}