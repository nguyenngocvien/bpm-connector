package com.bpm.api.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.adapter.service.AdapterService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/adapter")
@RequiredArgsConstructor
public class AdapterController {
	@Autowired
    private final AdapterService adapterService;

    @PostMapping("/{serviceCode}")
    public ResponseEntity<?> handleRequest(
        @PathVariable String serviceCode,
        @RequestBody Map<String, Object> input
    ) {
        Object result = adapterService.invoke(serviceCode, input);
        return ResponseEntity.ok(result);
    }
}