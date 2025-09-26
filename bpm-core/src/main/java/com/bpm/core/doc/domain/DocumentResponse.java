package com.bpm.core.doc.domain;

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
