package com.bpm.core.entity;

public class Response<T> {
    private int code;
    private String message;
    private T data;

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

    public static <T> Response<T> success() {
        return new Response<>(0, "OK", null);
    }
    
    public static <T> Response<T> success(T data) {
        return new Response<>(0, "OK", data);
    }

    public static <T> Response<T> error(String message) {
        return new Response<>(1, message, null);
    }
    
    public static <T> Response<T> error(int code, String message) {
        return new Response<>(code, message, null);
    }

    public static <T> Response<T> error(int code, String message, Long logId) {
        String fullMessage = (logId != null) ? message + " [LogId: " + logId + "]" : message;
        return new Response<>(code, fullMessage, null);
    }

    // Getters & Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
