package com.bpm.core.entity;

public class Response<T> {
    private int code;
    private String message;
    private T data;
    private Long logId;

    public Response() {}

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
    
    public Response(int code, String message, T data, Long logId) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.logId = logId;
    }

    public static <T> Response<T> success(T data) {
        return new Response<>(0, "Success", data, null);
    }
    
    public static <T> Response<T> success(String message, T data) {
        return new Response<>(0, message, data, null);
    }
    
    public static <T> Response<T> success(String message, T data, Long logId) {
        return new Response<>(0, message, data, logId);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(1, message, null, null);
    }

    public Response<T> withLogId(Long logId) {
        this.logId = logId;
        return this;
    }

    public static <T> Response<T> error(int code, String message) {
        return new Response<>(code, message, null, null);
    }
    
    public static <T> Response<T> error(int code, String message, long logId) {
        return new Response<>(code, message, null, logId);
    }

    // Getters & Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
	
	@Override
    public String toString() {
        String msgWithLogId = message + " LogID: " + (logId != null ? logId : "null");
        return "{" + code + ", " + msgWithLogId + ", " + data + "}";
    }
}
