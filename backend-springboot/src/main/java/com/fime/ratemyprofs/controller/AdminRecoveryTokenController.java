package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.dto.auth.RecoveryTokenInfo;
import com.fime.ratemyprofs.model.entity.PasswordRecoveryToken;
import com.fime.ratemyprofs.repository.PasswordRecoveryTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CONTROLADOR TEMPORAL SOLO PARA DESARROLLO
 * 
 * Este controlador permite a los admins consultar los tokens de recuperación
 * Esto es útil para testing ya que no hay servicio de email real
 * 
 * EN PRODUCCIÓN ESTE CONTROLADOR DEBE SER ELIMINADO POR SEGURIDAD
 */
@RestController
@RequestMapping("/api/admin/recovery-tokens")
@RequiredArgsConstructor
public class AdminRecoveryTokenController {

    private final PasswordRecoveryTokenRepository tokenRepository;

    /**
     * GET /api/admin/recovery-tokens
     * Lista todos los tokens de recuperación (solo para admins)
     * 
     * TEMPORAL: Solo para desarrollo/testing
     * En producción este endpoint NO debe existir
     */
    @GetMapping
    public ResponseEntity<List<RecoveryTokenInfo>> getAllTokens() {
        List<PasswordRecoveryToken> tokens = tokenRepository.findAllOrderByCreatedAtDesc();
        
        List<RecoveryTokenInfo> response = tokens.stream()
                .map(this::mapToTokenInfo)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/admin/recovery-tokens/user/{userId}
     * Lista todos los tokens de un usuario específico
     * 
     * TEMPORAL: Solo para desarrollo/testing
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RecoveryTokenInfo>> getUserTokens(@PathVariable Integer userId) {
        List<PasswordRecoveryToken> tokens = tokenRepository.findByUser_UserId(userId);
        
        List<RecoveryTokenInfo> response = tokens.stream()
                .map(this::mapToTokenInfo)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/admin/recovery-tokens/expired
     * Elimina todos los tokens expirados (limpieza)
     */
    @DeleteMapping("/expired")
    public ResponseEntity<String> cleanExpiredTokens() {
        tokenRepository.deleteExpiredTokens(LocalDateTime.now());
        return ResponseEntity.ok("Expired tokens cleaned successfully");
    }

    /**
     * Helper para mapear entidad a DTO
     */
    private RecoveryTokenInfo mapToTokenInfo(PasswordRecoveryToken token) {
        return RecoveryTokenInfo.builder()
                .tokenId(token.getTokenId())
                .token(token.getToken())
                .userId(token.getUser().getUserId())
                .userEmail(token.getUser().getEmail())
                .userName(token.getUser().getName())
                .expiresAt(token.getExpiresAt())
                .used(token.getUsed())
                .expired(token.isExpired())
                .valid(token.isValid())
                .createdAt(token.getCreatedAt())
                .build();
    }
}
