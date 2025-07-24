package com.bpm.api.controller.sql;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.api.constant.ROUTES;
import com.bpm.api.service.SQLService;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(ROUTES.SQL)
public class SQLController {

    private final SQLService sqlService;

    public SQLController(SQLService sqlService) {
        this.sqlService = sqlService;
    }

    @PostMapping("/query")
    public ResponseEntity<String> query(@RequestBody String jsonInput) {
        String resultJson = sqlService.handleQuery(jsonInput);
        return ResponseEntity.ok(resultJson);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody String jsonInput) {
        String resultJson = sqlService.handleUpdate(jsonInput);
        return ResponseEntity.ok(resultJson);
    }

    @PostMapping("/procedure")
    public ResponseEntity<String> procedure(@RequestBody String jsonInput) {
        String resultJson = sqlService.handleProcedure(jsonInput);
        return ResponseEntity.ok(resultJson);
    }
}
