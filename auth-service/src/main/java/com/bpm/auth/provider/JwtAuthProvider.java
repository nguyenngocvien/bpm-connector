package com.bpm.auth.provider;

import com.bpm.auth.AuthResult;
import com.bpm.auth.AuthenticationProvider;
import com.bpm.auth.jwt.JwtUtil;

public class JwtAuthProvider implements AuthenticationProvider {

    private final JwtUtil jwtUtil;

    public JwtAuthProvider(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supports(String authHeader) {
        return authHeader != null && authHeader.trim().startsWith("Bearer ");
    }

    @Override
    public AuthResult authenticate(String authHeader) {
        try {
            String token = authHeader.substring(7).trim();
            if (!jwtUtil.validateToken(token)) {
                return AuthResult.failure("Invalid JWT token");
            }

            String username = jwtUtil.extractUsername(token);
            return AuthResult.success(username);
        } catch (Exception e) {
            return AuthResult.failure("JWT parse error");
        }
    }
}
