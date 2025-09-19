package com.bpm.api.modules.document.dto;

import java.util.Map;

import lombok.Data;

@Data
public class DocumentRequest {
    private String documentCode;
    private Map<String, String> data;
}