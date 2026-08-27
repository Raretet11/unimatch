package com.rar.unimatch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.rar.unimatch.model.user.User;

import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    private static final String VERIFY_EMAIL_API_PATH = "/api/v1/auth/verify-email?token=";

    public void sendVerificationEmail(User user, String token) {
        Context context = buildContext(user, token);

        String htmlContent = templateEngine.process("email/verification-email", context);

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("Подтверждение email для UniMatch");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Verification email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send verification email to: {}", user.getEmail(), e);
            throw new RuntimeException("Failed to send verification email", e);
        }
    }

    private Context buildContext(User user, String token) {
        String verificationUrl = baseUrl + VERIFY_EMAIL_API_PATH + token;

        Context context = new Context();
        context.setVariable("username", user.getUsername());
        context.setVariable("verificationUrl", verificationUrl);
        context.setVariable("email", user.getEmail());
        context.setVariable("expiryHours", 24);
        context.setVariable("supportEmail", "support@unimatch.com");
        context.setVariable("year", java.time.Year.now().getValue());

        return context;
    }
}
