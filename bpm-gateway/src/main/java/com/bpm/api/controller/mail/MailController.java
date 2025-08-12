package com.bpm.api.controller.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bpm.api.constant.ROUTES;
import com.bpm.core.model.mail.MailServiceConfig;
import com.bpm.core.model.service.ServiceConfig;
import com.bpm.core.repository.Store;
import com.bpm.core.service.impl.EmailSender;

@RestController
@RequestMapping(ROUTES.MAIL)
public class MailController {

    private final Store store;
    private final EmailSender emailSender;

    @Autowired
    public MailController(Store store, EmailSender emailSender) {
        this.store = store;
        this.emailSender = emailSender;
    }

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestParam String emailCode,
                                  @RequestParam String listMailTo) {
        try {
            ServiceConfig config = store.serviceConfigs().findByCode(emailCode)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Email Code: " + emailCode));

            MailServiceConfig mailConfig = store.mailServices().findById(config.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid Email Code: " + emailCode));

            // Assign mailTo from listMailTo
            mailConfig.setMailTo(listMailTo);

            emailSender.sendEmail(mailConfig);

            return ResponseEntity.ok("Email sent successfully");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Failed to send email: " + ex.getMessage());
        }
    }
}
