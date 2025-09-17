package com.bpm.core.server.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.bpm.core.server.domain.Server;

import java.util.List;
import java.util.Optional;

@Repository
public class ServerRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Server> rowMapper = new BeanPropertyRowMapper<>(Server.class);

    public ServerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Server> findAll() {
        String sql = "SELECT * FROM core_servers ORDER BY id ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Server> findById(Long id) {
        String sql = "SELECT * FROM core_servers WHERE id = ?";
        List<Server> servers = jdbcTemplate.query(sql, rowMapper, id);
        return servers.stream().findFirst();
    }

    public void insert(Server server) {
        String sql = "INSERT INTO core_servers (name, type, ip, port, https) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                server.getName(),
                server.getType(),
                server.getIp(),
                server.getPort(),
                server.isHttps()
        );
    }

    public void update(Server server) {
        String sql = "UPDATE core_servers SET name = ?, type = ?, ip = ?, port = ?, https = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                server.getName(),
                server.getType(),
                server.getIp(),
                server.getPort(),
                server.isHttps(),
                server.getId()
        );
    }

    public void save(Server server) {
        if (server.getId() == null) {
            insert(server);
        } else {
            update(server);
        }
    }

    public void delete(Long id) {
        String sql = "DELETE FROM core_servers WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
