package com.bpm.core.entity;

import java.util.List;
import java.util.Map;

public class SQLProcedureRequest {
    private String datasource;
    private String name;
    private Map<String, Object> namedInParams;
    private List<String> outParams;

    // Constructors
    public SQLProcedureRequest() {}

    public SQLProcedureRequest(String datasource, String name, Map<String, Object> namedInParams, List<String> outParams) {
        this.datasource = datasource;
        this.name = name;
        this.namedInParams = namedInParams;
        this.outParams = outParams;
    }

    // Getters & Setters
    public String getDatasource() {
        return datasource;
    }

    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getNamedInParams() {
        return namedInParams;
    }

    public void setNamedInParams(Map<String, Object> namedInParams) {
        this.namedInParams = namedInParams;
    }

    public List<String> getOutParams() {
        return outParams;
    }

    public void setOutParams(List<String> outParams) {
        this.outParams = outParams;
    }

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