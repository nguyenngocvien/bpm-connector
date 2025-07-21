package com.bpm.core.entity;

import java.util.List;

public class SQLRequest {
    private String sql;
    private List<Object> params;
    private String path;
    private String name;
    private List<Object> inParams;
    private List<String> outParams;

    // Getters & Setters
    public String getSql() { return sql; }
    public void setSql(String sql) { this.sql = sql; }

    public List<Object> getParams() { return params; }
    public void setParams(List<Object> params) { this.params = params; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Object> getInParams() { return inParams; }
    public void setInParams(List<Object> inParams) { this.inParams = inParams; }

    public List<String> getOutParams() { return outParams; }
    public void setOutParams(List<String> outParams) { this.outParams = outParams; }
}
