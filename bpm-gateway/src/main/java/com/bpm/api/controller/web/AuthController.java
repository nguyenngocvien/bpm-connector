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
        List<AuthConfig> users = service.findAll();
        model.addAttribute("users", users);
        model.addAttribute("content", "user/list");
        model.addAttribute("activeMenu", "user");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("user", new AuthConfig());
        model.addAttribute("content", "user/form");
        model.addAttribute("activeMenu", "user");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        AuthConfig user = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        user.setPassword(""); // clear password for editing
        model.addAttribute("user", user);
        model.addAttribute("content", "user/form");
        model.addAttribute("activeMenu", "user");
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