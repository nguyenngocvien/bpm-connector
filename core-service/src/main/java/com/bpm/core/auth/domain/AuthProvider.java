package com.bpm.core.auth.domain;

public interface AuthProvider {
    boolean supports(String authHeader);
    AuthResult authenticate(String authHeader);
}