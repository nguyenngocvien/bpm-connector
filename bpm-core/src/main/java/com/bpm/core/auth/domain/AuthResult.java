package com.bpm.core.auth.domain;

public class AuthResult {
    private final boolean success;
    private final String username;
    private final String error;

    public AuthResult(boolean success, String username, String error) {
        this.success = success;
        this.username = username;
        this.error = error;
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

    // Convenience static methods
    public static AuthResult success(String username) {
        return new AuthResult(true, username, null);
    }

    public static AuthResult fail(String error) {
        return new AuthResult(false, null, error);
    }
}
