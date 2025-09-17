package com.bpm.core.document.domain;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocResponse {
    private String id;
    private String name;
    private String type;
    private String versionLabel;
    private List<String> paths;
    private String createdBy;
    private Date creationDate;
    private String lastModifiedBy;
}
