package com.bpm.core.server.service;

import com.bpm.core.server.domain.ServerConfig;
import com.bpm.core.server.domain.ServerType;
import com.bpm.core.server.repository.ServerConfigRepository;

import java.util.List;

public class ServerRepositoryService {

    private final ServerConfigRepository serverRepository;

    public ServerRepositoryService(ServerConfigRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public List<ServerConfig> getAllServers() {
        return serverRepository.findAll();
    }

    public ServerConfig getServerById(Long id) {
        ServerConfig server = serverRepository.findById(id)
        		.orElseThrow(() -> new IllegalArgumentException("Invalid ID")); 
    	return server;
    }
    
    public List<ServerConfig> getServersByType(ServerType type) {
        return serverRepository.findByType(type.name());
    }

    public void saveServer(ServerConfig server) {
        serverRepository.save(server);
    }

    public void deleteServer(Long id) {
        serverRepository.deleteById(id);
    }
}
