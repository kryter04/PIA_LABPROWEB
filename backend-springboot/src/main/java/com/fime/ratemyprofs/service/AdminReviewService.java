package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.review.ReviewResponse;
import com.fime.ratemyprofs.model.entity.Review;
import com.fime.ratemyprofs.model.entity.ReviewStatus;
import com.fime.ratemyprofs.repository.ReviewRepository;
import com.fime.ratemyprofs.repository.ReviewStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewStatusRepository reviewStatusRepository;
    private final ReviewService reviewService;

    /**
     * Obtiene todas las reseñas pendientes de aprobación
     * Solo para administradores
     */
    public Page<ReviewResponse> getPendingReviews(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<Review> reviews = reviewRepository.findByStatusName("Pending", pageable);
        
        return reviews.map(reviewService::mapToReviewResponse);
    }

    /**
     * Aprueba una reseña
     * Solo para administradores
     */
    @Transactional
    public ReviewResponse approveReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reseña no encontrada con ID: " + reviewId));

        ReviewStatus approvedStatus = reviewStatusRepository.findByStatusName("Approved")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estado 'Approved' no encontrado"));

        review.setStatus(approvedStatus);
        Review updatedReview = reviewRepository.save(review);
        
        return reviewService.mapToReviewResponse(updatedReview);
    }

    /**
     * Rechaza una reseña
     * Solo para administradores
     */
    @Transactional
    public ReviewResponse rejectReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reseña no encontrada con ID: " + reviewId));

        ReviewStatus rejectedStatus = reviewStatusRepository.findByStatusName("Rejected")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estado 'Rejected' no encontrado"));

        review.setStatus(rejectedStatus);
        Review updatedReview = reviewRepository.save(review);
        
        return reviewService.mapToReviewResponse(updatedReview);
    }
}
