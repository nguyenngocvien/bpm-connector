package com.bpm.auth;

public interface AuthenticationProvider {
    boolean supports(String authHeader);
    AuthResult authenticate(String authHeader);
}
