package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.BadRequestException;
import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.review.CreateReviewRequest;
import com.fime.ratemyprofs.model.dto.review.ReviewResponse;
import com.fime.ratemyprofs.model.entity.*;
import com.fime.ratemyprofs.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ProfessorRepository professorRepository;
    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final ReviewStatusRepository reviewStatusRepository;

    /**
     * Crea una nueva reseña para un profesor
     * La reseña se crea con estado "Pending" por defecto
     */
    @Transactional
    public ReviewResponse createReview(CreateReviewRequest request, Long userId) {
        // Validar rating (1-5)
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new BadRequestException("El rating debe estar entre 1 y 5");
        }

        // Validar que el profesor existe
        Professor professor = professorRepository.findById(request.getProfessorId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profesor no encontrado con ID: " + request.getProfessorId()));

        // Validar que la materia existe
        Subject subject = subjectRepository.findById(request.getSubjectId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Materia no encontrada con ID: " + request.getSubjectId()));

        // Obtener el usuario
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + userId));

        // Obtener el estado "Pending"
        ReviewStatus pendingStatus = reviewStatusRepository.findByStatusName("Pending")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estado 'Pending' no encontrado en la base de datos"));

        // Crear la reseña
        Review review = Review.builder()
                .user(user)
                .professor(professor)
                .subject(subject)
                .status(pendingStatus)
                .rating(request.getRating())
                .comment(request.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        // Guardar la reseña primero
        Review savedReview = reviewRepository.save(review);

        // Crear las imágenes si existen
        List<String> imageUrls = new ArrayList<>();
        if (request.getImageUrls() != null && !request.getImageUrls().isEmpty()) {
            // Validar que no haya más de 5 imágenes
            if (request.getImageUrls().size() > 5) {
                throw new BadRequestException("No se pueden agregar más de 5 imágenes por reseña");
            }

            for (String imageUrl : request.getImageUrls()) {
                ReviewImage image = ReviewImage.builder()
                        .review(savedReview)
                        .imageUrl(imageUrl)
                        .build();
                reviewImageRepository.save(image);
                imageUrls.add(imageUrl);
            }
        }

        // Retornar la respuesta
        return ReviewResponse.builder()
                .reviewId(savedReview.getReviewId())
                .userId(user.getUserId())
                .userName(user.getName())
                .professorId(professor.getProfessorId())
                .professorName(professor.getName())
                .subjectId(subject.getSubjectId())
                .subjectName(subject.getName())
                .rating(savedReview.getRating())
                .comment(savedReview.getComment())
                .imageUrls(imageUrls)
                .likeCount(0L)  // Nueva reseña, sin votos
                .dislikeCount(0L)
                .status(pendingStatus.getStatusName())
                .createdAt(savedReview.getCreatedAt())
                .build();
    }

    /**
     * Mapea una entidad Review a ReviewResponse
     * Helper method para reutilizar en otros servicios
     */
    public ReviewResponse mapToReviewResponse(Review review) {
        // Calcular likes y dislikes
        Long likes = reviewRepository.countLikesByReviewId(review.getReviewId());
        Long dislikes = reviewRepository.countDislikesByReviewId(review.getReviewId());

        // Obtener URLs de imágenes
        List<String> imageUrls = review.getImages().stream()
                .map(ReviewImage::getImageUrl)
                .toList();

        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .userId(review.getUser().getUserId())
                .userName(review.getUser().getName())
                .professorId(review.getProfessor().getProfessorId())
                .professorName(review.getProfessor().getName())
                .subjectId(review.getSubject().getSubjectId())
                .subjectName(review.getSubject().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .imageUrls(imageUrls)
                .likeCount(likes)
                .dislikeCount(dislikes)
                .status(review.getStatus().getStatusName())
                .createdAt(review.getCreatedAt())
                .build();
    }

    /**
     * Obtiene todas las reseñas con filtros opcionales
     * Endpoint público que por defecto solo muestra reseñas aprobadas
     * 
     * @param professorId Filtro opcional por ID de profesor
     * @param subjectId Filtro opcional por ID de materia
     * @param status Filtro opcional por estado (por defecto "Approved")
     * @param page Número de página (0-indexed)
     * @param size Tamaño de página
     * @return Página de reseñas
     */
    public Page<ReviewResponse> getAllReviews(
            Integer professorId, 
            Integer subjectId, 
            String status,
            int page, 
            int size) {
        
        // Por defecto, solo mostrar reseñas aprobadas
        String statusFilter = (status != null && !status.isEmpty()) ? status : "Approved";
        
        Pageable pageable = PageRequest.of(page, size);
        
        Page<Review> reviews = reviewRepository.findAllWithFilters(
            professorId, 
            subjectId, 
            statusFilter, 
            pageable
        );
        
        return reviews.map(this::mapToReviewResponse);
    }
}
