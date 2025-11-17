package com.fime.ratemyprofs.repository;

import com.fime.ratemyprofs.model.entity.PasswordRecoveryToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRecoveryTokenRepository extends JpaRepository<PasswordRecoveryToken, Integer> {

    /**
     * Busca un token válido (no usado y no expirado)
     */
    @Query("SELECT t FROM PasswordRecoveryToken t " +
           "WHERE t.token = :token " +
           "AND t.used = false " +
           "AND t.expiresAt > :now")
    Optional<PasswordRecoveryToken> findValidToken(
        @Param("token") String token,
        @Param("now") LocalDateTime now
    );

    /**
     * Busca token por string (sin validar si está usado o expirado)
     */
    Optional<PasswordRecoveryToken> findByToken(String token);

    /**
     * Lista todos los tokens de un usuario
     */
    List<PasswordRecoveryToken> findByUser_UserId(Integer userId);

    /**
     * Lista todos los tokens (para endpoint admin temporal)
     */
    @Query("SELECT t FROM PasswordRecoveryToken t ORDER BY t.createdAt DESC")
    List<PasswordRecoveryToken> findAllOrderByCreatedAtDesc();

    /**
     * Invalida (marca como usado) todos los tokens de un usuario
     */
    @Modifying
    @Query("UPDATE PasswordRecoveryToken t SET t.used = true WHERE t.user.userId = :userId AND t.used = false")
    void invalidateAllUserTokens(@Param("userId") Integer userId);

    /**
     * Elimina tokens expirados (limpieza)
     */
    @Modifying
    @Query("DELETE FROM PasswordRecoveryToken t WHERE t.expiresAt < :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);
}
