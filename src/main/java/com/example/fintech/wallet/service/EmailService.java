package com.example.fintech.wallet.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String to, String verificationToken) {
        String subject = "Verifica tu correo electrónico";
        String verificationUrl = "http://localhost:8080/api/auth/verify-email?token=" + verificationToken;
        String text = "¡Bienvenido!\n\nPor favor, haz clic en el siguiente enlace para verificar tu correo electrónico y activar tu cuenta:\n" + verificationUrl + "\n\nSi no creaste esta cuenta, puedes ignorar este mensaje.";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
} 