package com.bpm.api.controller.web;

import com.bpm.core.entity.AuthUser;
import com.bpm.core.repository.AuthUserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/user")
public class AuthUserController {

    private final AuthUserRepository service;
    private final PasswordEncoder passwordEncoder;

    public AuthUserController(AuthUserRepository service, PasswordEncoder passwordEncoder) {
        this.service = service;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<AuthUser> users = service.findAll();
        model.addAttribute("users", users);
        return "authuser_list";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("authUser", new AuthUser());
        return "authuser_form";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        AuthUser user = service.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
        user.setPassword(""); // clear password for editing
        model.addAttribute("authUser", user);
        return "authuser_form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute AuthUser user) {
        service.save(user, passwordEncoder);
        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteById(id);
        return "redirect:/user/list";
    }
}