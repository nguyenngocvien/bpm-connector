package com.bpm.api.controller.db;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.api.constant.ROUTES;
import com.bpm.api.model.SQLProcRequestPayload;
import com.bpm.api.model.SQLRequestPayload;
import com.bpm.api.service.SQLService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(ROUTES.SQL)
public class SQLController {

    private final SQLService service;

    public SQLController(SQLService service) {
        this.service = service;
    }

    @PostMapping("/query")
    public ResponseEntity<String> query(@Valid @RequestBody SQLRequestPayload payload) {
        String resultJson = service.executeQuery(payload);
        return ResponseEntity.ok(resultJson);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@Valid @RequestBody SQLRequestPayload payload) {
        String resultJson = service.executeUpdate(payload);
        return ResponseEntity.ok(resultJson);
    }

    @PostMapping("/procedure")
    public ResponseEntity<String> procedure(@Valid @RequestBody SQLProcRequestPayload payload) {
        String resultJson = service.executeProcedure(payload);
        return ResponseEntity.ok(resultJson);
    }
}
