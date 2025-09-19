package com.bpm.core.server.service;

import com.bpm.core.server.domain.Server;
import com.bpm.core.server.repository.ServerConfigRepository;

import java.util.List;

public class ServerRepositoryService {

    private final ServerConfigRepository serverRepository;

    public ServerRepositoryService(ServerConfigRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    public Server getServerById(Long id) {
        Server server = serverRepository.findById(id)
        		.orElseThrow(() -> new IllegalArgumentException("Invalid ID")); 
    	return server;
    }

    public void saveServer(Server server) {
        serverRepository.save(server);
    }

    public void deleteServer(Long id) {
        serverRepository.deleteById(id);
    }
}
