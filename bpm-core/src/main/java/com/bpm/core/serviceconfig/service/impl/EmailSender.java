package com.bpm.core.serviceconfig.service.impl;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

import com.bpm.core.auth.domain.AuthConfig;
import com.bpm.core.auth.service.AuthRepositoryService;
import com.bpm.core.common.response.Response;
import com.bpm.core.mail.domain.MailServiceConfig;
import com.bpm.core.mail.service.MailServiceConfigService;
import com.bpm.core.server.domain.Server;
import com.bpm.core.server.service.ServerRepositoryService;

public class EmailSender {
	private final MailServiceConfigService mailService;
	private final ServerRepositoryService serverService;
	private final AuthRepositoryService authService;
	
	public EmailSender(MailServiceConfigService mailService, ServerRepositoryService serverService, AuthRepositoryService authService) {
		this.mailService = mailService;
		this.serverService = serverService;
		this.authService = authService;
	}

	public Object sendEmail(Long mailId, String jsonObject) {
	    try {
	        MailServiceConfig config = mailService.getActiveMailServiceConfigById(mailId);
	        Server server = serverService.getServerById(config.getServerId());
	        AuthConfig auth = authService.getAuthConfigById(config.getAuthId());

	        Properties props = new Properties();
	        props.put("mail.smtp.host", server.getIp());
	        props.put("mail.smtp.port", String.valueOf(server.getPort()));
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.starttls.enable", String.valueOf(server.isHttps()));

	        // Setup timeout
	        props.put("mail.smtp.connectiontimeout", config.getTimeoutMs().toString());
	        props.put("mail.smtp.timeout", config.getTimeoutMs().toString());
	        props.put("mail.smtp.writetimeout", config.getTimeoutMs().toString());

	        Session session = Session.getInstance(props, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(auth.getUsername(), auth.getPassword());
	            }
	        });

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(config.getDefaultMailFrom()));

	        // mailTo separate by comma
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(config.getDefaultMailTo()));

	        if (config.getDefaultMailCc() != null && !config.getDefaultMailCc().isEmpty()) {
	            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(config.getDefaultMailCc()));
	        }
	        if (config.getDefaultMailBcc() != null && !config.getDefaultMailBcc().isEmpty()) {
	            message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(config.getDefaultMailBcc()));
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

	        Transport.send(message);

	        return "Email sent successfully";

	    } catch (MessagingException e) {
	        return Response.error("Failed to send email: " + e.getMessage());
	    } catch (Exception e) {
	        return Response.error("Unexpected error: " + e.getMessage());
	    }
	}
}


//package com.bpm.core.invoker;
//
//import com.bpm.core.common.model.Response;
//import com.bpm.core.entity.ServiceConfig;
//import com.bpm.core.entity.ServiceType;
//import com.bpm.core.service.EmailConfigService;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class EmailInvoker implements ServiceInvoker {
//    private final EmailConfigService emailService;
//
//    public EmailInvoker(EmailConfigService emailService) {
//        this.emailService = emailService;
//    }
//
//    @Override
//    public ServiceType getServiceType() {
//        return ServiceType.EMAIL;
//    }
//
//    @Override
//    public Response execute(ServiceConfig config, String params) {
//        try {
//            emailService.sendEmail(config, params);
//            return Response.success("Email sent");
//        } catch (Exception e) {
//            log.error("Email send failed: {}", e.getMessage(), e);
//            return Response.error("EMAIL_ERROR", e.getMessage());
//        }
//    }
//}
//
