package com.darya.bookshelf.service;

import com.darya.bookshelf.dto.AuthResponse;
import com.darya.bookshelf.dto.LoginRequest;
import com.darya.bookshelf.dto.RegisterRequest;
import com.darya.bookshelf.entity.User;
import com.darya.bookshelf.entity.VerificationToken;
import com.darya.bookshelf.repository.UserRepository;
import com.darya.bookshelf.repository.VerificationTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final RecaptchaService recaptchaService;
    public AuthService(UserRepository userRepository, VerificationTokenRepository tokenRepository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService, RecaptchaService recaptchaService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.recaptchaService = recaptchaService;
    }

    @Transactional
    public String register(RegisterRequest request) {
        if (!recaptchaService.verifyCaptcha(request.getCaptchaResponse())) {
            return "Captcha verification failed";
        }
        if (userRepository.findByEmail(request.getEmail()) != null) {
            return "User already exists";
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        tokenRepository.save(verificationToken);
        try {
            emailService.sendVerificationEmail(user.getEmail(), token);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
        return "Registration successful. Check email.";
    }

    @Transactional
    public String verify(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if (verificationToken == null) {
            return "Invalid token";
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        tokenRepository.delete(verificationToken);
        return "Email verified successfully";
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }
        if (!user.isEnabled()) {
            throw new RuntimeException("Email not verified");
        }
        String jwt = jwtService.generateToken(user.getEmail());
        return new AuthResponse(jwt);
    }
}