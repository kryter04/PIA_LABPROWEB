package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.dto.review.ReviewResponse;
import com.fime.ratemyprofs.model.dto.user.UpdateUserRequest;
import com.fime.ratemyprofs.model.dto.user.UserResponse;
import com.fime.ratemyprofs.security.CustomUserDetailsService;
import com.fime.ratemyprofs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * GET /api/users/{userId}
     * Obtiene el perfil público de un usuario
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserProfile(@PathVariable Integer userId) {
        UserResponse user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * PUT /api/users/{userId}
     * Actualiza el perfil del usuario autenticado
     * Solo puede actualizar su propio perfil
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUserProfile(
            @PathVariable Integer userId,
            @Valid @RequestBody UpdateUserRequest request,
            Authentication authentication) {
        
        // Obtener el userId del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Integer authenticatedUserId = userDetailsService.getUserIdByEmail(email);
        
        UserResponse updatedUser = userService.updateUserProfile(userId, request, authenticatedUserId);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * GET /api/users/{userId}/reviews
     * Obtiene todas las reseñas del usuario
     */
    @GetMapping("/{userId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getUserReviews(@PathVariable Integer userId) {
        List<ReviewResponse> reviews = userService.getUserReviews(userId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * DELETE /api/users/{userId}/reviews/{reviewId}
     * Elimina una reseña del usuario
     * Solo el dueño de la reseña puede eliminarla
     */
    @DeleteMapping("/{userId}/reviews/{reviewId}")
    public ResponseEntity<Void> deleteUserReview(
            @PathVariable Integer userId,
            @PathVariable Integer reviewId,
            Authentication authentication) {
        
        // Obtener el userId del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Integer authenticatedUserId = userDetailsService.getUserIdByEmail(email);
        
        userService.deleteUserReview(userId, reviewId, authenticatedUserId);
        return ResponseEntity.noContent().build();
    }
}
