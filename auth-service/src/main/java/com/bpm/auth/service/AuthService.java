package com.bpm.auth.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bpm.auth.dto.User;
import com.bpm.auth.repository.UserRepository;
import com.bpm.auth.util.JWTUtil;

@Service
public class AuthService {
    @Autowired private UserRepository userRepo;
    @Autowired private JWTUtil jwtUtil;
    @Autowired private PasswordEncoder passwordEncoder;

    public String login(String username, String rawPassword) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        return jwtUtil.generateToken(user.getUsername(), claims);
    }
}