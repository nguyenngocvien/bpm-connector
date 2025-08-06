package com.bpm.api.controller.web;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.db.DataSourceConfig;
import com.bpm.core.repository.Store;
import com.bpm.core.util.DataSourceTestUtil;

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

    private final Store store;

    public DataSourceConfigController(Store store) {
        this.store = store;
    }

    @GetMapping
    public String list(Model model) {
        List<DataSourceConfig> configs = store.datasources().findAll();
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
        DataSourceConfig config = store.datasources().findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("dbConfig", config);
        model.addAttribute("content", "db/form");
        model.addAttribute("activeMenu", "datasource");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DataSourceConfig config) {
    	store.datasources().save(config);
        return "redirect:" + ROUTES.UI_DATASOURCE;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
    	store.datasources().deleteById(id);
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
}
