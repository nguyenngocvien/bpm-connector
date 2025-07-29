package com.bpm.api.model;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class SQLRequestPayload {
	@NotBlank(message = "Datasource is required")
    private String datasource;

    @NotBlank(message = "SQL is required")
    private String sql;

    private List<Object> params;

	public String getDatasource() {
		return datasource;
	}

	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}
    
}