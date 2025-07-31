package com.bpm.core.model.rest;

public class RestHeader {
	private Long id;
    private Long restConfigId;
    private String headerName;
    private String headerValue;

    public RestHeader(Long id, Long restConfigId, String headerName, String headerValue) {
        this.id = id;
        this.restConfigId = restConfigId;
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    public RestHeader(Long id, String headerName, String headerValue) {
    	this.id = id;
		this.headerName = headerName;
		this.headerValue = headerValue;
	}

	public Long getId() { return id; }
    public Long getRestConfigId() { return restConfigId; }
    public String getHeaderName() { return headerName; }
    public String getHeaderValue() { return headerValue; }
}
