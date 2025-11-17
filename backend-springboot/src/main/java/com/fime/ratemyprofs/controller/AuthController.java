package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.dto.auth.AuthResponse;
import com.fime.ratemyprofs.model.dto.auth.LoginRequest;
import com.fime.ratemyprofs.model.dto.auth.PasswordRecoveryRequest;
import com.fime.ratemyprofs.model.dto.auth.PasswordRecoveryResponse;
import com.fime.ratemyprofs.model.dto.auth.PasswordResetRequest;
import com.fime.ratemyprofs.model.dto.auth.RegisterRequest;
import com.fime.ratemyprofs.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/password/recovery
     * Inicia el proceso de recuperación de contraseña
     * Endpoint público (no requiere autenticación)
     * Genera un token temporal guardado en BD con expiración de 1 hora
     * Por seguridad, siempre retorna 200 OK aunque el email no exista
     */
    @PostMapping("/password/recovery")
    public ResponseEntity<PasswordRecoveryResponse> recoverPassword(
            @Valid @RequestBody PasswordRecoveryRequest request) {
        try {
            PasswordRecoveryResponse response = authService.initiatePasswordRecovery(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Por seguridad, no revelar si el email existe o no
            // Siempre retornar el mismo mensaje
            PasswordRecoveryResponse response = PasswordRecoveryResponse.builder()
                    .message("If your email exists in our system, you will receive password recovery instructions")
                    .email(request.getEmail())
                    .recoveryToken(null) // NUNCA retornar el token
                    .build();
            return ResponseEntity.ok(response);
        }
    }

    /**
     * POST /api/password/reset
     * Resetea la contraseña usando un token válido
     * Endpoint público (no requiere autenticación)
     * Valida el token y actualiza la contraseña del usuario
     */
    @PostMapping("/password/reset")
    public ResponseEntity<PasswordRecoveryResponse> resetPassword(
            @Valid @RequestBody PasswordResetRequest request) {
        PasswordRecoveryResponse response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
