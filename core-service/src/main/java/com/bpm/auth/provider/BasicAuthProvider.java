package com.bpm.auth.provider;

import java.util.Base64;

import org.springframework.stereotype.Component;

import com.bpm.auth.AuthProperties;
import com.bpm.auth.AuthProvider;
import com.bpm.auth.AuthResult;

@Component
public class BasicAuthProvider implements AuthProvider {

    private final AuthProperties authProperties;

    public BasicAuthProvider(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Override
    public boolean supports(String authHeader) {
        return authHeader != null && authHeader.startsWith("Basic ");
    }

    @Override
    public AuthResult authenticate(String authHeader) {
        try {
            String base64 = authHeader.substring(6).trim();
            String[] parts = new String(Base64.getDecoder().decode(base64)).split(":", 2);
            if (parts.length != 2) {
                return AuthResult.fail("Invalid Basic Auth format");
            }
            String username = parts[0];
            String password = parts[1];
            String stored = authProperties.getBasicUsers().get(username);
            if (stored != null && stored.equals(password)) {
                return AuthResult.success(username);
            }
        } catch (Exception e) {
            return AuthResult.fail("Error parsing Basic Auth: " + e.getMessage());
        }
        return AuthResult.fail("Invalid credentials");
    }
}