package com.bpm.core.db.domain;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SQLProcedureRequest {
    private String datasource;
    private String name;
    private Map<String, Object> namedInParams;
    private List<String> outParams;

    @Override
    public String toString() {
        return "SQLProcRequestPayload{" +
                "datasource='" + datasource + '\'' +
                ", name='" + name + '\'' +
                ", namedInParams=" + namedInParams +
                ", outParams=" + outParams +
                '}';
    }
}
