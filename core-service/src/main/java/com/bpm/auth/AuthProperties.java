package com.bpm.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    private String jwtSecret;
    private Map<String, String> basicUsers = new HashMap<>();

    public String getJwtSecret() {
        return jwtSecret;
    }

    public void setJwtSecret(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public Map<String, String> getBasicUsers() {
        return basicUsers;
    }

    public void setBasicUsers(Map<String, String> basicUsers) {
        this.basicUsers = basicUsers;
    }
}