package com.bpm.api.modules.document.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.core.common.response.Response;
import com.bpm.core.serviceconfig.service.ServiceDispatcher;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/doc")
public class DocumentController {

    private final ServiceDispatcher serviceDispatcher;

    @Autowired
    public DocumentController(ServiceDispatcher serviceDispatcher) {
        this.serviceDispatcher = serviceDispatcher;
    }

    @PostMapping("/generate")
    public ResponseEntity<Response<Object>> generateDocument(
            @RequestParam String serviceCode,
            @RequestBody String paramsJson) {

        Response<Object> response = serviceDispatcher.executeServiceByCode(serviceCode, paramsJson);

        return ResponseEntity.ok(response);
    }
}

