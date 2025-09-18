package com.bpm.api;

import org.springframework.stereotype.Component;

import com.bpm.core.db.cache.DataSourceCache;

import jakarta.annotation.PreDestroy;

@Component
public class AppShutdownManager {

    @PreDestroy
    public void onShutdown() {
        System.out.println("Shutting down, clearing DataSource cache...");
        DataSourceCache.clearCache();
    }
}
