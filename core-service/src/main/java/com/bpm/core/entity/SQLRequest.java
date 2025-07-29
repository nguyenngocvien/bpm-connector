package com.bpm.core.entity;

import java.util.List;

public class SQLRequest {
	private String datasource;
    private String sql;
    private List<Object> params;
    
    public SQLRequest(String datasource, String sql, List<Object> params) {
		this.datasource = datasource;
		this.sql = sql;
		this.params = params;
	}

    // Getters & Setters
    public String getDatasource() { return datasource; }
    public void setDatasource(String datasource) { this.datasource = datasource; }
    
    public String getSql() { return sql; }
    public void setSql(String sql) { this.sql = sql; }

    public List<Object> getParams() { return params; }
    public void setParams(List<Object> params) { this.params = params; }
    
    @Override
    public String toString() {
        return "SQLRequest{" +
                "datasource='" + datasource + '\'' +
                ", sql='" + sql + '\'' +
                ", params=" + params +
                '}';
    }
}
