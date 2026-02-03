package com.projetimmo.projet_immobilier.controller;

import com.projetimmo.projet_immobilier.dto.LoginRequest;
import com.projetimmo.projet_immobilier.dto.LoginResponse;
import com.projetimmo.projet_immobilier.dto.RegisterRequest;
import com.projetimmo.projet_immobilier.service.interfaces.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Utilisateur créé avec succès");
    }

    @GetMapping("/test")
    public String test() {
        return "OK";
    }
}
