package com.bpm.api.controller.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.service.impl.EmailService;

@RestController
@RequestMapping(ROUTES.MAIL)
public class MailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestParam String to,
                                  @RequestParam String subject,
                                  @RequestParam String message) {
        emailService.sendEmail(to, subject, message);
        return ResponseEntity.ok("Email sent");
    }
}