package com.bpm.api.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bpm.core.entity.AuthUser;
import com.bpm.core.repository.AuthUserRepository;

@Controller
@RequestMapping("/ui/users")
public class AuthUserUIController {

    @Autowired
    private AuthUserRepository repository;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", repository.findAll());
        return "user_list";  // user_list.html
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("user", new AuthUser());
        return "user_form";  // user_form.html
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute AuthUser user,
                           @Autowired PasswordEncoder encoder) {
        if (user.getId() == null) {
            repository.insert(user, encoder);
        } else {
            repository.update(user, encoder);
        }
        return "redirect:/ui/users";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
    	AuthUser user = repository.findById(id)
    		    .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "user_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        repository.delete(id);
        return "redirect:/ui/users";
    }
}
