package com.bpm.api.controller.web;

import com.bpm.core.config.DbConfig;
import com.bpm.core.repository.DbConfigRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/dbconfig")
public class DbConfigController {

    private final DbConfigRepository service;

    public DbConfigController(DbConfigRepository service) {
        this.service = service;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<DbConfig> configs = service.findAll();
        model.addAttribute("configs", configs);
        return "dbconfig_list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("dbConfig", new DbConfig());
        return "dbconfig_form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        DbConfig config = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        model.addAttribute("dbConfig", config);
        return "dbconfig_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DbConfig config) {
        service.save(config);
        return "redirect:/dbconfig/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/dbconfig/list";
    }
}
