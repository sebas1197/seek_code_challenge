package com.seek.presentation.controller;

import com.seek.infrastructure.security.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Authentication", description = "Endpoints para registro e inicio de sesión de usuarios")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;

    @Data
    public static class AuthRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Data
    public static class AuthResponse {
        @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        private String token;
        public AuthResponse(String token) { this.token = token; }
    }

    @Operation(
        summary = "Iniciar sesión",
        description = "Valida credenciales de usuario y devuelve un token JWT válido."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        String token = authService.login(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @Operation(
        summary = "Registrar usuario",
        description = "Crea un nuevo usuario en el sistema (solo para pruebas)."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuario registrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody AuthRequest req) {
        authService.register(req.getUsername(), req.getPassword());
    }
}
