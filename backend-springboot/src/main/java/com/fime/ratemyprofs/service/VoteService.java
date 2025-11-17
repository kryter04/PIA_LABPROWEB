package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.BadRequestException;
import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.vote.VoteRequest;
import com.fime.ratemyprofs.model.dto.vote.VoteResponse;
import com.fime.ratemyprofs.model.entity.Review;
import com.fime.ratemyprofs.model.entity.ReviewVote;
import com.fime.ratemyprofs.model.entity.User;
import com.fime.ratemyprofs.model.entity.VoteType;
import com.fime.ratemyprofs.repository.ReviewRepository;
import com.fime.ratemyprofs.repository.ReviewVoteRepository;
import com.fime.ratemyprofs.repository.UserRepository;
import com.fime.ratemyprofs.repository.VoteTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final ReviewVoteRepository reviewVoteRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final VoteTypeRepository voteTypeRepository;

    /**
     * Registra o actualiza un voto (like/dislike) en una reseña
     * Si el usuario ya votó, se actualiza el tipo de voto
     * Si es el mismo tipo, se elimina el voto
     */
    @Transactional
    public VoteResponse voteReview(Integer reviewId, VoteRequest request, Integer userId) {
        // Validar que la reseña existe
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Reseña no encontrada con ID: " + reviewId));

        // Validar que el usuario existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + userId));

        // Validar que el tipo de voto es válido (Like o Dislike)
        String voteTypeName = request.getVoteType();
        if (!voteTypeName.equals("Like") && !voteTypeName.equals("Dislike")) {
            throw new BadRequestException("Tipo de voto inválido. Debe ser 'Like' o 'Dislike'");
        }

        // Obtener el VoteType by voteName
        VoteType voteType = voteTypeRepository.findByVoteName(voteTypeName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de voto no encontrado: " + voteTypeName));

        // Verificar si el usuario ya votó en esta reseña
        ReviewVote existingVote = reviewVoteRepository
                .findByReview_ReviewIdAndUser_UserId(reviewId, userId)
                .orElse(null);

        if (existingVote != null) {
            // Si el usuario ya votó con el mismo tipo, eliminar el voto (toggle)
            if (existingVote.getVoteType().getVoteName().equals(voteTypeName)) {
                reviewVoteRepository.delete(existingVote);
                return buildVoteResponse(review, userId, null);
            } else {
                // Si votó con otro tipo, actualizar el voto
                existingVote.setVoteType(voteType);
                reviewVoteRepository.save(existingVote);
                return buildVoteResponse(review, userId, voteTypeName);
            }
        } else {
            // Si no ha votado, crear nuevo voto
            try {
                ReviewVote newVote = ReviewVote.builder()
                        .review(review)
                        .user(user)
                        .voteType(voteType)
                        .build();
                reviewVoteRepository.save(newVote);
                return buildVoteResponse(review, userId, voteTypeName);
            } catch (DataIntegrityViolationException e) {
                throw new BadRequestException("Error al registrar el voto. Posible voto duplicado.");
            }
        }
    }

    /**
     * Helper method para construir la respuesta del voto
     */
    private VoteResponse buildVoteResponse(Review review, Integer userId, String currentUserVote) {
        Long likes = reviewRepository.countLikesByReviewId(review.getReviewId());
        Long dislikes = reviewRepository.countDislikesByReviewId(review.getReviewId());
        int totalVotes = (int)(likes - dislikes);

        return VoteResponse.builder()
                .message(currentUserVote != null ? "Voto registrado" : "Voto eliminado")
                .reviewId(review.getReviewId())
                .voteType(currentUserVote)
                .totalVotes(totalVotes)
                .build();
    }
}
