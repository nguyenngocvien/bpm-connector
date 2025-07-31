package com.bpm.api.controller.web;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.db.DataSourceConfig;
import com.bpm.core.repository.DataSourceRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_DATASOURCE)
public class DataSourceConfigController {

    private final DataSourceRepository service;

    public DataSourceConfigController(DataSourceRepository service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        List<DataSourceConfig> configs = service.findAll();
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
        DataSourceConfig config = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("dbConfig", config);
        model.addAttribute("content", "db/form");
        model.addAttribute("activeMenu", "datasource");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DataSourceConfig config) {
        service.save(config);
        return "redirect:" + ROUTES.UI_DATASOURCE;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return "redirect:" + ROUTES.UI_DATASOURCE;
    }
}
