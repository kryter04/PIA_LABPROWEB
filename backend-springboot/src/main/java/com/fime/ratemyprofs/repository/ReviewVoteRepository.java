package com.fime.ratemyprofs.repository;

import com.fime.ratemyprofs.model.entity.ReviewVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewVoteRepository extends JpaRepository<ReviewVote, Integer> {

    @Query("SELECT rv FROM ReviewVote rv " +
           "WHERE rv.user.userId = :userId " +
           "AND rv.review.reviewId = :reviewId " +
           "AND rv.voteType.voteTypeId = :voteTypeId")
    Optional<ReviewVote> findByUserAndReviewAndVoteType(
        @Param("userId") Integer userId,
        @Param("reviewId") Integer reviewId,
        @Param("voteTypeId") Integer voteTypeId
    );

    @Query("SELECT SUM(vt.voteValue) FROM ReviewVote rv " +
           "JOIN rv.voteType vt " +
           "WHERE rv.review.reviewId = :reviewId")
    Integer calculateTotalVotesByReview(@Param("reviewId") Integer reviewId);

    @Query("SELECT COUNT(rv) FROM ReviewVote rv " +
           "WHERE rv.review.reviewId = :reviewId " +
           "AND rv.voteType.voteName = :voteName")
    Long countVotesByReviewAndType(
        @Param("reviewId") Integer reviewId,
        @Param("voteName") String voteName
    );

    boolean existsByUserUserIdAndReviewReviewIdAndVoteTypeVoteTypeId(
        Integer userId,
        Integer reviewId,
        Integer voteTypeId
    );

    /**
     * Busca un voto existente por reviewId y userId
     * Útil para verificar si un usuario ya votó en una reseña
     */
    Optional<ReviewVote> findByReview_ReviewIdAndUser_UserId(Long reviewId, Long userId);
}
