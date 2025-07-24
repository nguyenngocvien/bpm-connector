package com.bpm.auth;

public interface AuthProvider {
    boolean supports(String authHeader);
    AuthResult authenticate(String authHeader);
}