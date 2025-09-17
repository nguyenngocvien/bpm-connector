package com.bpm.api.modules.document.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.document.domain.UploadForm;

@Controller
@RequestMapping(ROUTES.UI_FILE)
public class FileController {

    @GetMapping("/")
    public String index(Model model) {
        UploadForm form = new UploadForm();
        model.addAttribute("form", form);
        model.addAttribute("content", "file/upload");
        model.addAttribute("activeMenu", "file");
        return "main";
    }
}