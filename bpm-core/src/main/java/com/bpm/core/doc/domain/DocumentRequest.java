package com.bpm.core.doc.domain;

import java.util.Map;

import lombok.Data;

@Data
public class DocumentRequest {
    private String documentCode;
    private Map<String, String> data;
}