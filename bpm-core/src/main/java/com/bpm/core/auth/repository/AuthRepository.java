package com.bpm.core.auth.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.domain.AuthType;
import com.bpm.core.common.util.AESCryptoUtil;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class AuthRepository {

    private final JdbcTemplate jdbcTemplate;

    public AuthRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AuthConfig> findAll() {
        String sql = "SELECT * FROM core_service_auth";
        return jdbcTemplate.query(sql, authRowMapper());
    }

    public Optional<AuthConfig> findById(Integer id) {
        String sql = "SELECT * FROM core_service_auth WHERE id = ?";
        try {
            AuthConfig auth = jdbcTemplate.queryForObject(sql, authRowMapper(), id);
            return Optional.ofNullable(auth);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<AuthConfig> findByName(String name) {
        String sql = "SELECT * FROM core_service_auth WHERE name = ?";
        try {
            AuthConfig auth = jdbcTemplate.queryForObject(sql, authRowMapper(), name);
            return Optional.ofNullable(auth);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int save(AuthConfig config) {
        String encryptedPassword = AESCryptoUtil.encrypt(config.getPassword());
        String encryptedApiKeyValue = AESCryptoUtil.encrypt(config.getApiKeyValue());
        String encryptedClientSecret = AESCryptoUtil.encrypt(config.getOauth2ClientSecret());

        if (config.getId() == null) {
            // INSERT
            String sql = "INSERT INTO core_service_auth (name, auth_type, username, password, token, api_key_header, api_key_value, oauth2_client_id, oauth2_client_secret, oauth2_token_url, role, scope, active) VALUES (?, ?::auth_type_enum, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(sql,
                    config.getName(),
                    config.getAuthType().name(),
                    config.getUsername(),
                    encryptedPassword,
                    config.getToken(),
                    config.getApiKeyHeader(),
                    encryptedApiKeyValue,
                    config.getOauth2ClientId(),
                    encryptedClientSecret,
                    config.getOauth2TokenUrl(),
                    config.getRole(),
                    config.getScope(),
                    config.isActive());
        } else {
            // UPDATE
            String sql = "UPDATE core_service_auth SET name = ?, auth_type = ?::auth_type_enum, username = ?, password = ?, token = ?, api_key_header = ?, api_key_value = ?, oauth2_client_id = ?, oauth2_client_secret = ?, oauth2_token_url = ?, role = ?, scope = ?, active = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";

            return jdbcTemplate.update(sql,
                    config.getName(),
                    config.getAuthType().name(),
                    config.getUsername(),
                    encryptedPassword,
                    config.getToken(),
                    config.getApiKeyHeader(),
                    encryptedApiKeyValue,
                    config.getOauth2ClientId(),
                    encryptedClientSecret,
                    config.getOauth2TokenUrl(),
                    config.getRole(),
                    config.getScope(),
                    config.isActive(),
                    config.getId());
        }
    }

    public int deleteById(Long id) {
        String sql = "DELETE FROM core_service_auth WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private RowMapper<AuthConfig> authRowMapper() {
        return (rs, rowNum) -> {
            AuthConfig config = new AuthConfig();
            config.setId(rs.getInt("id"));
            config.setName(rs.getString("name"));
            try {
                config.setAuthType(AuthType.valueOf(rs.getString("auth_type")));
            } catch (IllegalArgumentException e) {
                config.setAuthType(AuthType.NONE);
            }

            config.setUsername(rs.getString("username"));

            String encryptedPassword = rs.getString("password");
            config.setPassword(AESCryptoUtil.decrypt(encryptedPassword));

            config.setToken(rs.getString("token"));
            config.setApiKeyHeader(rs.getString("api_key_header"));

            String encryptedApiKeyValue = rs.getString("api_key_value");
            config.setApiKeyValue(AESCryptoUtil.decrypt(encryptedApiKeyValue));

            config.setOauth2ClientId(rs.getString("oauth2_client_id"));

            String encryptedClientSecret = rs.getString("oauth2_client_secret");
            config.setOauth2ClientSecret(AESCryptoUtil.decrypt(encryptedClientSecret));

            config.setOauth2TokenUrl(rs.getString("oauth2_token_url"));
            config.setRole(rs.getString("role"));
            config.setScope(rs.getString("scope"));
            config.setActive(rs.getBoolean("active"));

            Timestamp createdTs = rs.getTimestamp("created_at");
            config.setCreatedAt(createdTs != null ? createdTs.toLocalDateTime() : null);
            Timestamp updatedTs = rs.getTimestamp("updated_at");
            config.setUpdatedAt(updatedTs != null ? updatedTs.toLocalDateTime() : null);

            return config;
        };
    }
}
