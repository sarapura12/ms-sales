package com.salemanager.application.service.interfaces;

import jakarta.mail.MessagingException;

import java.util.Map;

public interface IEmailService {
    void sendMail(String to, String subject, Map<String, Object> variables) throws MessagingException;
}
