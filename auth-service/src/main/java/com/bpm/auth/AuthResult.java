package com.bpm.auth;

public class AuthResult {
    private boolean success;
    private String username;
    private String error;

    private AuthResult(boolean success, String username, String error) {
        this.success = success;
        this.username = username;
        this.error = error;
    }

    public static AuthResult success(String username) {
        return new AuthResult(true, username, null);
    }

    public static AuthResult failure(String error) {
        return new AuthResult(false, null, error);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getUsername() {
        return username;
    }

    public String getError() {
        return error;
    }
}

