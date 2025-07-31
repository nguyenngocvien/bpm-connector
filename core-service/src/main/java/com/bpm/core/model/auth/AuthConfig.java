package com.bpm.core.model.auth;

import java.time.LocalDateTime;

public class AuthConfig {

    private Integer id;
    private String name;
    private AuthType authType;

    private String username;
    private String password;

    private String token;

    private String apiKeyHeader;
    private String apiKeyValue;

    private String oauth2ClientId;
    private String oauth2ClientSecret;
    private String oauth2TokenUrl;

    private String role;
    private String scope;

    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Builder ---
    public static class Builder {
        private final String name;
        private final AuthType authType;

        private String username;
        private String password;
        private String token;
        private String apiKeyHeader;
        private String apiKeyValue;
        private String oauth2ClientId;
        private String oauth2ClientSecret;
        private String oauth2TokenUrl;
        private String role;
        private String scope;

        public Builder(String name, AuthType authType) {
            this.name = name;
            this.authType = authType;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder apiKey(String header, String value) {
            this.apiKeyHeader = header;
            this.apiKeyValue = value;
            return this;
        }

        public Builder oauth2(String clientId, String clientSecret, String tokenUrl) {
            this.oauth2ClientId = clientId;
            this.oauth2ClientSecret = clientSecret;
            this.oauth2TokenUrl = tokenUrl;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder scope(String scope) {
            this.scope = scope;
            return this;
        }

        public AuthConfig build() {
            AuthConfig config = new AuthConfig();
            config.setName(this.name);
            config.setAuthType(this.authType);
            config.setUsername(this.username);
            config.setPassword(this.password);
            config.setToken(this.token);
            config.setApiKeyHeader(this.apiKeyHeader);
            config.setApiKeyValue(this.apiKeyValue);
            config.setOauth2ClientId(this.oauth2ClientId);
            config.setOauth2ClientSecret(this.oauth2ClientSecret);
            config.setOauth2TokenUrl(this.oauth2TokenUrl);
            config.setRole(this.role);
            config.setScope(this.scope);
            config.setActive(true);
            config.setCreatedAt(LocalDateTime.now());
            config.setUpdatedAt(LocalDateTime.now());
            return config;
        }
    }
  
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOauth2ClientId() {
		return oauth2ClientId;
	}

	public void setOauth2ClientId(String oauth2ClientId) {
		this.oauth2ClientId = oauth2ClientId;
	}

	public String getOauth2ClientSecret() {
		return oauth2ClientSecret;
	}

	public void setOauth2ClientSecret(String oauth2ClientSecret) {
		this.oauth2ClientSecret = oauth2ClientSecret;
	}

	public String getOauth2TokenUrl() {
		return oauth2TokenUrl;
	}

	public void setOauth2TokenUrl(String oauth2TokenUrl) {
		this.oauth2TokenUrl = oauth2TokenUrl;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getApiKeyHeader() {
		return apiKeyHeader;
	}

	public void setApiKeyHeader(String apiKeyHeader) {
		this.apiKeyHeader = apiKeyHeader;
	}

	public String getApiKeyValue() {
		return apiKeyValue;
	}

	public void setApiKeyValue(String apiKeyValue) {
		this.apiKeyValue = apiKeyValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public AuthType getAuthType() {
		return authType;
	}

	public void setAuthType(AuthType authType) {
		this.authType = authType;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}