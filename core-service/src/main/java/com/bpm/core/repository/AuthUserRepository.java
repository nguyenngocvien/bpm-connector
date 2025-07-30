package com.bpm.core.repository;

import com.bpm.core.entity.AuthUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthUserRepository {

	@Autowired
    private final JdbcTemplate jdbcTemplate;

    public AuthUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    public List<AuthUser> findAll() {
        String sql = "SELECT * FROM core_auth_user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            AuthUser user = new AuthUser();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            return user;
        });
    }
    
    public Optional<AuthUser> findById(Long id) {
        String sql = "SELECT * FROM core_auth_user WHERE id = ?";
        try {
            AuthUser user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                AuthUser u = new AuthUser();
                u.setId(rs.getLong("id"));
                u.setUsername(rs.getString("username"));
                u.setPassword("");
                u.setRole(rs.getString("role"));
                return u;
            }, id);

            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<AuthUser> findByUsername(String username) {
        String sql = "SELECT * FROM core_auth_user WHERE username = ?";

        try {
        	AuthUser user = jdbcTemplate.queryForObject(sql, userRowMapper(), username);
            return Optional.ofNullable(user);
        } catch (Exception e) {
            // Optional: log error, e.g., logger.warn("User not found: {}", username);
            return Optional.empty();
        }
    }
    
    public int save(AuthUser user, PasswordEncoder encoder) {
    	if (user.getId() == null) {
    		// INSERT
    		String sql = "INSERT INTO core_auth_user (username, password, role) VALUES (?, ?, ?)";
            return jdbcTemplate.update(sql,
                    user.getUsername(),
                    encoder.encode(user.getPassword()),  // mã hóa mật khẩu
                    user.getRole());
    	} else {
            // UPDATE
    		String sql = "UPDATE core_auth_user SET username = ?, password = ?, role = ? WHERE id = ?";
            return jdbcTemplate.update(sql,
                    user.getUsername(),
                    encoder.encode(user.getPassword()),
                    user.getRole(),
                    user.getId());
    	}
        
    }
    
    public int deleteById(Long id) {
        String sql = "DELETE FROM core_auth_user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    private RowMapper<AuthUser> userRowMapper() {
        return (rs, rowNum) -> {
            AuthUser user = new AuthUser();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
            return user;
        };
    }
}
