package com.bpm.api.controller.sql;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.service.SQLConnector;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping(ROUTES.SQL)
public class SQLController {

	private final SQLConnector sqlConnector;

    public SQLController(SQLConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }

    @PostMapping("/query")
    public ResponseEntity<String> query(@RequestBody String jsonInput) {
        String resultJson = sqlConnector.executeQuery(jsonInput);
        return ResponseEntity.ok(resultJson);
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody String jsonInput) {
        String resultJson = sqlConnector.executeUpdate(jsonInput);
        return ResponseEntity.ok(resultJson);
    }

    @PostMapping("/procedure")
    public ResponseEntity<String> procedure(@RequestBody String jsonInput) {
        String resultJson = sqlConnector.executeProcedure(jsonInput);
        return ResponseEntity.ok(resultJson);
    }
}
