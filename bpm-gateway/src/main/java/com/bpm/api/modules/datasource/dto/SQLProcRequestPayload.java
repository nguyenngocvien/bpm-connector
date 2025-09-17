package com.bpm.api.modules.datasource.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public class SQLProcRequestPayload {

    @NotBlank(message = "Datasource is required")
    private String datasource;

    @NotBlank(message = "Procedure name is required")
    private String name;

    @NotNull(message = "namedInParams must not be null")
    private Map<String, Object> namedInParams;

    @NotNull(message = "outParams must not be null")
    private List<String> outParams;

    // Constructors
    public SQLProcRequestPayload() {}

    public SQLProcRequestPayload(String datasource, String name, Map<String, Object> namedInParams, List<String> outParams) {
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
