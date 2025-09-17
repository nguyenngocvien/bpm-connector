package com.bpm.api.modules.document.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.repository.CmisRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping(ROUTES.MULTIPART_DOCUMENT)
public class MultipartDocumentController {

    private final CmisRepository service;

    public MultipartDocumentController(CmisRepository service) {
        this.service = service;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, Object>> create(
            @RequestParam String folderPath,
            @RequestParam String name,
            @RequestParam(required = false, defaultValue = "cmis:document") String typeId,
            @RequestParam MultipartFile file,
            @RequestParam(required = false) String propertiesJson
    ) throws IOException {
        Map<String, Object> customProps = new HashMap<>();
        if (propertiesJson != null && !propertiesJson.isBlank()) {
            ObjectMapper mapper = new ObjectMapper();
            customProps = mapper.readValue(propertiesJson, new TypeReference<Map<String, Object>>() {});
        }

        String docId = service.createDocument(file, folderPath, typeId, customProps);
        return ResponseEntity.ok(Map.of("id", docId, "props", customProps));
    }
}