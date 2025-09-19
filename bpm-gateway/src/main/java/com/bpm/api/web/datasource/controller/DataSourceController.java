package com.bpm.api.web.datasource.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.core.db.cache.DataSourceCache;

@RestController
@RequestMapping("/admin/datasource")
public class DataSourceController {

    @PostMapping("/clear-cache")
    public ResponseEntity<String> clearCache() {
        DataSourceCache.clearCache();
        return ResponseEntity.ok("DataSource cache cleared.");
    }
}
