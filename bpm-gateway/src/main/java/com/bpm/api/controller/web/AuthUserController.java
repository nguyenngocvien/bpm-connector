package com.bpm.api.controller.web;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.entity.AuthUser;
import com.bpm.core.repository.AuthUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(ROUTES.UI_AUTH_USER)
public class AuthUserController {

    private final AuthUserRepository service;
    private final PasswordEncoder passwordEncoder;

    public AuthUserController(AuthUserRepository service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(Model model) {
        List<AuthUser> users = service.findAll();
        model.addAttribute("users", users);
        model.addAttribute("content", "user/list");
        model.addAttribute("activeMenu", "user");
        return "main";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("user", new AuthUser());
        model.addAttribute("content", "user/form");
        model.addAttribute("activeMenu", "user");
        return "main";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        AuthUser user = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        user.setPassword(""); // clear password for editing
        model.addAttribute("user", user);
        model.addAttribute("content", "user/form");
        model.addAttribute("activeMenu", "user");
        return "main";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AuthUser user) {
        service.save(user, passwordEncoder);
        return "redirect:" + ROUTES.UI_AUTH_USER;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:" + ROUTES.UI_AUTH_USER;
    }
}