package com.bpm.core.datasource.domain;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SQLRequest {
    private String datasource;
    private String sql;
    private List<Object> params;

    @Override
    public String toString() {
        return "SQLRequest{" +
                "datasource='" + datasource + '\'' +
                ", sql='" + sql + '\'' +
                ", params=" + params +
                '}';
    }
}
