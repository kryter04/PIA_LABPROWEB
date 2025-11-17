package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.dto.admin.UpdateReviewRequest;
import com.fime.ratemyprofs.model.dto.review.ReviewResponse;
import com.fime.ratemyprofs.service.AdminReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/reviews")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    /**
     * GET /api/admin/reviews
     * Lista todas las reseñas pendientes de aprobación (solo administradores)
     * Query params:
     * - page: Número de página (default: 0)
     * - size: Tamaño de página (default: 10)
     */
    @GetMapping
    public ResponseEntity<Page<ReviewResponse>> getPendingReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ReviewResponse> reviews = adminReviewService.getPendingReviews(page, size);
        return ResponseEntity.ok(reviews);
    }

    /**
     * PUT /api/admin/reviews/{reviewId}/approve
     * Aprueba una reseña (solo administradores)
     */
    @PutMapping("/{reviewId}/approve")
    public ResponseEntity<ReviewResponse> approveReview(@PathVariable Long reviewId) {
        ReviewResponse review = adminReviewService.approveReview(reviewId);
        return ResponseEntity.ok(review);
    }

    /**
     * PUT /api/admin/reviews/{reviewId}/reject
     * Rechaza una reseña (solo administradores)
     */
    @PutMapping("/{reviewId}/reject")
    public ResponseEntity<ReviewResponse> rejectReview(@PathVariable Long reviewId) {
        ReviewResponse review = adminReviewService.rejectReview(reviewId);
        return ResponseEntity.ok(review);
    }

    /**
     * PUT /api/admin/reviews/{reviewId}
     * Edita el contenido de una reseña (rating y comentario)
     * Solo para administradores
     */
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody UpdateReviewRequest request) {
        ReviewResponse review = adminReviewService.updateReview(reviewId, request);
        return ResponseEntity.ok(review);
    }
}
