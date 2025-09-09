package com.bpm.core.model.fncmis;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocRequest {
    private String folderPath;
    private String name;
    
    @Default
    private String typeId = "cmis:document";
    private String base64Content;
    private String mimeType; // ex: application/pdf
    private Map<String, Object> properties;
}