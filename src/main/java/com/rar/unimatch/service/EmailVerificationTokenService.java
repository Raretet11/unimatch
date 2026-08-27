package com.rar.unimatch.service;

import com.rar.unimatch.error.BadRequestException;
import com.rar.unimatch.model.user.EmailVerificationToken;
import com.rar.unimatch.model.user.User;
import com.rar.unimatch.repository.EmailVerificationTokenRepository;
import com.rar.unimatch.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationTokenService {
    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private static final int TOKEN_EXPIRY_HOURS = 24;

    @Transactional
    public EmailVerificationToken createVerificationToken(User user) {
        tokenRepository.findByUser(user).ifPresent(token -> {
            tokenRepository.delete(token);
            log.info("Deleted old verification token for user: {}", user.getUsername());
        });

        String tokenValue = generateToken();
        EmailVerificationToken token = EmailVerificationToken.builder()
            .token(tokenValue)
            .user(user)
            .expiryDate(LocalDateTime.now().plusHours(TOKEN_EXPIRY_HOURS))
            .confirmed(false)
            .build();
        
        EmailVerificationToken savedToken = tokenRepository.save(token);
        log.info("Created verification token for user: {}, expires at: {}", 
            user.getUsername(), savedToken.getExpiryDate());
        
        return savedToken;
    }

    @Transactional
    public User verifyToken(String tokenValue) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenValue)
            .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        if (token.isConfirmed()) {
            throw new BadRequestException("Email already verified");
        }

        if (token.isExpired()) {
            throw new BadRequestException("Verification token has expired. Please request a new one.");
        }

        token.setConfirmed(true);
        tokenRepository.save(token);

        User user = token.getUser();
        user.setEmailVerified(true);
        userRepository.save(user);

        log.info("Email verified successfully for user: {}", user.getUsername());
        return user;
    }

    public boolean isValidToken(String tokenValue) {
        return tokenRepository.findByToken(tokenValue)
            .map(token -> !token.isConfirmed() && !token.isExpired())
            .orElse(false);
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
