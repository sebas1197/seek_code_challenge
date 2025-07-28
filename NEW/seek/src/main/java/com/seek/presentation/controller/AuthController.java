package com.seek.presentation.controller;

import com.seek.infrastructure.security.AuthenticationService;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints públicos de autenticación (login y registro opcional).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    /* ---- DTOs internos ---- */
    @Data
    public static class AuthRequest {
        @NotBlank String username;
        @NotBlank String password;
    }
    @Data
    public static class AuthResponse {
        String token;
        AuthResponse(String token) { this.token = token; }
    }

    /* ---- Endpoints ---- */

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        String token = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    /* Registro opcional (útil para pruebas) */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody AuthRequest req) {
        authService.register(req.getUsername(), req.getPassword());
    }
}
