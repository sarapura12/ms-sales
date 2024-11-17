package com.salemanager.application.service.impl;

import com.salemanager.application.service.interfaces.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Service
public class EmailServiceImpl implements IEmailService {

    @Value("${spring.mail.username}")
    private String from;
    @Value("${email.logo.path}")
    private String logoPath;


    private final TemplateEngine templateEngine;
    private JavaMailSender mailSender;

    public EmailServiceImpl(TemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(String to, String subject, Map<String, Object> variables) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("Confirmación de compra");

        Context context = new Context();
        context.setVariables(variables);
        String htmlContent = templateEngine.process("purchaseMailTemplate", context);
        helper.setText(htmlContent, true);
        ClassPathResource image = new ClassPathResource(logoPath);
        helper.addInline("logo", image);

        helper.setTo(to);
        helper.setFrom(from);
        mailSender.send(mimeMessage);

    }
}