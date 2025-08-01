package com.bpm.core.model.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DbParamConfig {

    private Integer id;
    private Long dbConfigId;

    private String paramName;
    private String paramType;    // STRING, INT, DATE...
    private String paramMode;    // IN, OUT
    private Integer paramOrder;
}
