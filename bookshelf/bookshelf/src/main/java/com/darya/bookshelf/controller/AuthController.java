package com.darya.bookshelf.controller;

import com.darya.bookshelf.dto.AuthResponse;
import com.darya.bookshelf.dto.LoginRequest;
import com.darya.bookshelf.dto.RegisterRequest;
import com.darya.bookshelf.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "API для регистрации, верификации и авторизации")
public class AuthController {

    private final AuthService service;
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    @Operation(summary = "Регистрация нового пользователя", description = "Создаёт пользователя и отправляет email для подтверждения")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        String result = service.register(request);
        if (result.contains("successful")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/verify")
    @Operation(summary = "Подтверждение email", description = "Активирует аккаунт по токену из письма")
    public ResponseEntity<String> verify(@RequestParam String token) {
        String result = service.verify(token);
        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @PostMapping("/login")
    @Operation(summary = "Авторизация", description = "Возвращает JWT токен для доступа к API")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            AuthResponse response = service.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}