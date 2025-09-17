package com.bpm.api.modules.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.mail.service.EmailSender;
import com.bpm.core.serviceconfig.domain.ServiceConfig;
import com.bpm.core.serviceconfig.service.ServiceConfigService;

@RestController
@RequestMapping(ROUTES.MAIL)
public class MailController {

    private final ServiceConfigService service;
    private final EmailSender emailSender;

    @Autowired
    public MailController(ServiceConfigService service, EmailSender emailSender) {
        this.service = service;
        this.emailSender = emailSender;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestParam String emailCode,
                                  @RequestParam String listMailTo) {
        try {
            ServiceConfig config = service.findByCode(emailCode);

            // Assign mailTo from listMailTo
            config.getMailServiceConfig().setMailTo(listMailTo);

            emailSender.sendEmail(config.getMailServiceConfig());

            return ResponseEntity.ok("Email sent successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to send email: " + ex.getMessage());
        }
    }
}
