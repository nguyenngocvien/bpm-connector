package com.bpm.api.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bpm.api.constant.ROUTES;

@Controller
@RequestMapping(ROUTES.UI)
public class UiController {

    @GetMapping
    public String home(Model model) {
        model.addAttribute("content", "welcome");
        return "main";
    }
}