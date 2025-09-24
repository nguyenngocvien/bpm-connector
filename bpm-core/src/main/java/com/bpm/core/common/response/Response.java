package com.bpm.core.common.response;

import org.springframework.http.HttpStatus;

public class Response {
	private int status;
	private String code;
	private String message;
	private String data;
	private String logId;

    public Response() {}
    
    public Response(int status, String code, String message, String data, String logId) {
    	this.status = status;
    	this.code = code;
        this.message = message;
        this.data = data;
        this.logId = logId;
    }
    
    public static Response success(String data) {
        return new Response(HttpStatus.OK.value(), "SUCCESS", "OK", data, null);
    }
    
    public static Response error(String message) {
        return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "ERROR", message, null, null);
    }
    
    public static Response error(String code, String message) {
    	return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), code, message, null, null);
	}
    
    public static Response error(String code, String message, String logId) {
    	return new Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), code, message, null, logId);
	}
    
    public static Response error(int status, String code, String message) {
        return new Response(status, code, message, null, null);
    }
    
    public static Response error(int status, String code, String message, String logId) {
        return new Response(status, code, message, null, logId);
    }

    public Response withLogId(Long logId) {
        if (logId != null) {
            this.logId = String.valueOf(logId);
        }
        return this;
    }
    
    // Getters & Setters
    public int getStatus() {
		return status;
	}
    public void setStatus(int status) {
		this.status = status;
	}
    
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getLogId() { return logId; }
    public void setLogId(String logId) { this.logId = logId; }
}
