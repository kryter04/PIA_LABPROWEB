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
     * Lista reseñas filtradas por estado (solo administradores)
     * Query params:
     * - status: Estado de las reseñas (Pending, Approved, Rejected, o vacío para todas)
     * - page: Número de página (default: 0)
     * - size: Tamaño de página (default: 10)
     */
    @GetMapping
    public ResponseEntity<Page<ReviewResponse>> getReviews(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ReviewResponse> reviews = adminReviewService.getReviews(status, page, size);
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

    /**
     * DELETE /api/admin/reviews/{reviewId}
     * Elimina una reseña (los votos e imágenes se eliminan automáticamente)
     * Solo para administradores
     */
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        adminReviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
