package com.bpm.core.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import org.springframework.stereotype.Service;

import com.bpm.core.model.mail.MailServiceConfig;

@Service("emailSender")
public class EmailSender {

    public void sendEmail(MailServiceConfig config) throws MessagingException {
        if (config == null || !Boolean.TRUE.equals(config.getActive())) {
            throw new IllegalArgumentException("MailServiceConfig is null or inactive");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", config.getSmtpServer());
        props.put("mail.smtp.port", String.valueOf(config.getSmtpPort()));
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", config.getUseTls().toString());

        // Setup timeout
        props.put("mail.smtp.connectiontimeout", config.getTimeoutMs().toString());
        props.put("mail.smtp.timeout", config.getTimeoutMs().toString());
        props.put("mail.smtp.writetimeout", config.getTimeoutMs().toString());

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(config.getMailFrom()));

        // mailTo seperate by comma
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(config.getMailTo()));

        if (config.getMailCc() != null && !config.getMailCc().isEmpty()) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(config.getMailCc()));
        }
        if (config.getMailBcc() != null && !config.getMailBcc().isEmpty()) {
            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(config.getMailBcc()));
        }

        message.setSubject(config.getSubjectTemplate());
        message.setText(config.getBodyTemplate());

        // handle custom header
        if (config.getHeaders() != null && !config.getHeaders().isEmpty()) {
            String[] headersArr = config.getHeaders().split(";");
            for (String header : headersArr) {
                String[] kv = header.split(":");
                if (kv.length == 2) {
                    message.setHeader(kv[0].trim(), kv[1].trim());
                }
            }
        }

        // Send mail
        Transport.send(message);
    }
}
