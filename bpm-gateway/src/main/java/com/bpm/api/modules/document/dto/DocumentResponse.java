package com.bpm.api.modules.document.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentResponse {
    private String fileName;
    private String extension;
    private String url;
    private String base64;
}
