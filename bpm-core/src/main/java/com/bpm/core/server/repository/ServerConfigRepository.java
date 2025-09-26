package com.bpm.core.server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bpm.core.server.domain.ServerConfig;

public interface ServerConfigRepository extends JpaRepository<ServerConfig, Long> {

	ServerConfig findByName(String name);

	ServerConfig findByIpAndPort(String ip, Integer port);
	
	List<ServerConfig> findByType(String type);
}
