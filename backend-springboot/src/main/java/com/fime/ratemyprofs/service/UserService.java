package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.BadRequestException;
import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.review.ReviewResponse;
import com.fime.ratemyprofs.model.dto.user.UpdateUserRequest;
import com.fime.ratemyprofs.model.dto.user.UserResponse;
import com.fime.ratemyprofs.model.entity.Review;
import com.fime.ratemyprofs.model.entity.User;
import com.fime.ratemyprofs.repository.ReviewRepository;
import com.fime.ratemyprofs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Obtiene el perfil de un usuario por su ID
     */
    public UserResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + userId));

        return mapToUserResponse(user);
    }

    /**
     * Actualiza el perfil de un usuario
     * Solo permite actualizar nombre y contraseña
     * El usuario solo puede actualizar su propio perfil
     */
    @Transactional
    public UserResponse updateUserProfile(Long userId, UpdateUserRequest request, Long authenticatedUserId) {
        // Verificar que el usuario autenticado está actualizando su propio perfil
        if (!userId.equals(authenticatedUserId)) {
            throw new BadRequestException("No tienes permiso para actualizar este perfil");
        }

        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + userId));

        // Actualizar nombre si se proporciona
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        // Actualizar contraseña si se proporciona
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            if (request.getPassword().length() < 6) {
                throw new BadRequestException("La contraseña debe tener al menos 6 caracteres");
            }
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return mapToUserResponse(updatedUser);
    }

    /**
     * Obtiene todas las reseñas de un usuario
     */
    public List<ReviewResponse> getUserReviews(Integer userId) {
        // Verificar que el usuario existe
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con ID: " + userId);
        }

        List<Review> reviews = reviewRepository.findByUser_UserId(userId);
        
        return reviews.stream()
                .map(reviewService::mapToReviewResponse)
                .collect(Collectors.toList());
    }

    /**
     * Elimina una reseña del usuario
     * Solo el dueño de la reseña puede eliminarla
     */
    @Transactional
    public void deleteUserReview(Long userId, Long reviewId, Long authenticatedUserId) {
        // Verificar que el usuario autenticado está eliminando su propia reseña
        if (!userId.equals(authenticatedUserId)) {
            throw new BadRequestException("No tienes permiso para eliminar esta reseña");
        }

        Review review = reviewRepository.findById(reviewId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reseña no encontrada con ID: " + reviewId));

        // Verificar que la reseña pertenece al usuario
        if (!review.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Esta reseña no pertenece al usuario especificado");
        }

        reviewRepository.delete(review);
    }

    /**
     * Helper method para mapear User a UserResponse
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
