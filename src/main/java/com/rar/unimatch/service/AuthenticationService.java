package com.rar.unimatch.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rar.unimatch.model.DTO.JwtAuthenticationResponse;
import com.rar.unimatch.model.DTO.SignInRequest;
import com.rar.unimatch.model.DTO.SignUpRequest;
import com.rar.unimatch.model.user.Role;
import com.rar.unimatch.model.user.User;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final EmailVerificationTokenService tokenService;

    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .build();

        userService.create(user);
        var token = tokenService.createVerificationToken(user);
        emailService.sendVerificationEmail(user, token.getToken());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.username());

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse verifyEmail(String token) {
        User verifiedUser = tokenService.verifyToken(token);
        String jwt = jwtService.generateToken(verifiedUser);

        log.info("Email verified for user: {}, with email: {}", verifiedUser.getUsername(), verifiedUser.getEmail());
        return new JwtAuthenticationResponse(jwt);
    }
}
