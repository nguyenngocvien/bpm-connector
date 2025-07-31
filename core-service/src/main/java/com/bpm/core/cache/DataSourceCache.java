package com.bpm.core.cache;

import com.bpm.core.model.db.DataSourceConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;

public class DataSourceCache {
    private static final ConcurrentHashMap<String, DataSource> cache = new ConcurrentHashMap<>();

    public static DataSource getOrCreate(DataSourceConfig config) {
        return cache.computeIfAbsent(config.getUrl(), k -> config.toDataSource());
    }

    public static void clearCache() {
        for (DataSource ds : cache.values()) {
            if (ds instanceof HikariDataSource) {
                ((HikariDataSource) ds).close();
            }
        }
        cache.clear();
    }

    public static boolean contains(String name) {
        return cache.containsKey(name);
    }
}
