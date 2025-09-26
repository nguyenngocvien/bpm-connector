package com.bpm.api.web.datasource.controller;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.db.cache.DataSourceCache;
import com.bpm.core.db.domain.DataSourceConfig;
import com.bpm.core.db.infrastructure.DataSourceTestUtil;
import com.bpm.core.db.service.DataSourceConfigService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(ROUTES.UI_DATASOURCE)
public class DataSourceConfigController {

    private final DataSourceConfigService dataSourceService;

    public DataSourceConfigController(DataSourceConfigService dataSourceService) {
        this.dataSourceService = dataSourceService;
    }

    @GetMapping
    public String list(Model model) {
        List<DataSourceConfig> configs = dataSourceService.getAllDataSources();
        model.addAttribute("dbConfigs", configs);
        model.addAttribute("content", "db/list");
        model.addAttribute("activeMenu", "datasource");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("dbConfig", new DataSourceConfig());
        model.addAttribute("content", "db/form");
        model.addAttribute("activeMenu", "datasource");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        DataSourceConfig config = dataSourceService.getDataSourceById(id);
        model.addAttribute("dbConfig", config);
        model.addAttribute("content", "db/form");
        model.addAttribute("activeMenu", "datasource");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DataSourceConfig config) {
    	dataSourceService.saveDataSource(config);
        return "redirect:" + ROUTES.UI_DATASOURCE;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
    	dataSourceService.deleteDataSource(id);
        return "redirect:" + ROUTES.UI_DATASOURCE;
    }
    
    @PostMapping("/test")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testConnection(@RequestBody DataSourceConfig config) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean success = DataSourceTestUtil.testConnection(config);
            response.put("success", success);
            response.put("message", success ? "OK" : "Connection failed");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
    
    @PostMapping("/clear-cache")
    public ResponseEntity<String> clearCache() {
        DataSourceCache.clearCache();
        return ResponseEntity.ok("DataSource cache cleared.");
    }
}
