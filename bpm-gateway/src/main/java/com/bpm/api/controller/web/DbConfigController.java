package com.bpm.api.controller.web;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.entity.DbConfig;
import com.bpm.core.repository.DbConfigRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_DB_CONFIG)
public class DbConfigController {

    private final DbConfigRepository service;

    public DbConfigController(DbConfigRepository service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        List<DbConfig> configs = service.findAll();
        model.addAttribute("dbConfigs", configs);
        model.addAttribute("content", "db/list");
        model.addAttribute("activeMenu", "datasource");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("dbConfig", new DbConfig());
        model.addAttribute("content", "db/form");
        model.addAttribute("activeMenu", "datasource");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        DbConfig config = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("dbConfig", config);
        model.addAttribute("content", "db/form");
        model.addAttribute("activeMenu", "datasource");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DbConfig config) {
        service.save(config);
        return "redirect:" + ROUTES.UI_DB_CONFIG;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:" + ROUTES.UI_DB_CONFIG;
    }
}
