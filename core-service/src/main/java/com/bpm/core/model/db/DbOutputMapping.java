package com.bpm.core.model.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DbOutputMapping {

    private Integer id;
    private Long dbConfigId;

    private String columnName;
    private String outputField;
}
