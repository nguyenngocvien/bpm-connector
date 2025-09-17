package com.bpm.core.common.response;

public class Wrapper<T> {
    private final int code;
    private final String message;
    private final T data;

    public Wrapper(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}