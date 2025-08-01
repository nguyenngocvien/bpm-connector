package com.bpm.api.controller.web;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.auth.AuthConfig;
import com.bpm.core.repository.AuthRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_AUTH_USER)
public class AuthController {

    private final AuthRepository service;

    public AuthController(AuthRepository service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        List<AuthConfig> authList = service.findAll();
        model.addAttribute("authList", authList);
        model.addAttribute("content", "auth/list");
        model.addAttribute("activeMenu", "auth");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("authConfig", new AuthConfig());
        model.addAttribute("content", "auth/form");
        model.addAttribute("activeMenu", "auth");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        AuthConfig authConfig = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        authConfig.setPassword(""); // clear password for editing
        model.addAttribute("authConfig", authConfig);
        model.addAttribute("content", "auth/form");
        model.addAttribute("activeMenu", "auth");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AuthConfig auth) {
        service.save(auth);
        return "redirect:" + ROUTES.UI_AUTH_USER;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.deleteById(id);
        return "redirect:" + ROUTES.UI_AUTH_USER;
    }
}