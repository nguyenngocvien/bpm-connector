package com.bpm.core.model.rest;

public class RestPathParam {
	private Long id;
    private Long restConfigId;
    private String paramName;
    private String paramValue;

    public RestPathParam(Long id, Long restConfigId, String paramName, String paramValue) {
        this.id = id;
        this.restConfigId = restConfigId;
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public RestPathParam(Long id, String paramName, String paramValue) {
    	this.id = id;
    	this.paramName = paramName;
        this.paramValue = paramValue;
	}

	public Long getId() { return id; }
    public Long getRestConfigId() { return restConfigId; }
    public String getParamName() { return paramName; }
    public String getParamValue() { return paramValue; }
}
