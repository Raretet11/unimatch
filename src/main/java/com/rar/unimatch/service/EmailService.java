package com.rar.unimatch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url")
    private String baseUrl;

    @Async
    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);

        log.info("Email sent successfully to: \"" + to + "\"");
    }

    public void sendWelcomeEmail(String to, String username) {
        String subject = "Добро пожаловать в UniMatch! 🎉";
        String text = String.format("""
            Здравствуйте, %s!

            Аккаунт на UniMatch успешно создан.

            С уважением,
            Команда UniMatch
            """,
            username,
            username
        );
        sendSimpleEmail(to, subject, text);
    }
}
