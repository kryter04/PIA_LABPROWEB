package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.dto.review.CreateReviewRequest;
import com.fime.ratemyprofs.model.dto.review.ReviewResponse;
import com.fime.ratemyprofs.model.dto.vote.VoteRequest;
import com.fime.ratemyprofs.model.dto.vote.VoteResponse;
import com.fime.ratemyprofs.security.CustomUserDetailsService;
import com.fime.ratemyprofs.service.ReviewService;
import com.fime.ratemyprofs.service.VoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final VoteService voteService;
    private final CustomUserDetailsService userDetailsService;

    /**
     * POST /api/reviews
     * Crea una nueva reseña para un profesor
     * Requiere autenticación (JWT token)
     * La reseña se crea con estado "Pending" y debe ser aprobada por un administrador
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication) {
        
        // Obtener el userId del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Integer userId = userDetailsService.getUserIdByEmail(email);
        
        ReviewResponse review = reviewService.createReview(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    /**
     * POST /api/reviews/{reviewId}/vote
     * Registra o actualiza un voto (like/dislike) en una reseña
     * Requiere autenticación (JWT token)
     * Si el usuario ya votó con el mismo tipo, se elimina el voto (toggle)
     * Si votó con otro tipo, se actualiza el voto
     */
    @PostMapping("/{reviewId}/vote")
    public ResponseEntity<VoteResponse> voteReview(
            @PathVariable Integer reviewId,
            @Valid @RequestBody VoteRequest request,
            Authentication authentication) {
        
        // Obtener el userId del usuario autenticado
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        Integer userId = userDetailsService.getUserIdByEmail(email);
        
        VoteResponse response = voteService.voteReview(reviewId, request, userId);
        return ResponseEntity.ok(response);
    }
}
