package com.bpm.auth.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bpm.auth.dto.User;
import com.bpm.core.entity.Response;
import com.bpm.core.entity.SQLRequest;
import com.bpm.core.service.SQLConnector;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepository {

    private final SQLConnector sqlConnector;
    private final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(SQLConnector sqlConnector, ObjectMapper objectMapper) {
        this.sqlConnector = sqlConnector;
        this.objectMapper = objectMapper;
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM int_auth WHERE username = ?";
        SQLRequest request = new SQLRequest();
        request.setSql(sql);
        request.setParams(Collections.singletonList(username));

        try {
            String responseJson = sqlConnector.executeQuery(objectMapper.writeValueAsString(request));
            Response<List<Map<String, Object>>> response = objectMapper.readValue(
                responseJson, new TypeReference<Response<List<Map<String, Object>>>>() {}
            );

            List<Map<String, Object>> data = response.getData();
            if (data.isEmpty()) return Optional.empty();

            Map<String, Object> row = data.get(0);
            User user = new User();
            user.setId(((Number) row.get("id")).longValue());
            user.setUsername((String) row.get("username"));
            user.setPassword((String) row.get("password"));
            user.setRole((String) row.get("role"));

            return Optional.of(user);

        } catch (Exception e) {
            logger.warn("Failed to find user by SQLConnector: {}", username, e);
            return Optional.empty();
        }
    }
}