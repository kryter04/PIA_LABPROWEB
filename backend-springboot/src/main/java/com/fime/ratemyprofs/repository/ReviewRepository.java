package com.fime.ratemyprofs.repository;

import com.fime.ratemyprofs.model.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query("SELECT r FROM Review r " +
           "WHERE r.professor.professorId = :professorId " +
           "AND r.status.statusName = 'Approved' " +
           "ORDER BY r.createdAt DESC")
    Page<Review> findApprovedReviewsByProfessor(
        @Param("professorId") Integer professorId,
        Pageable pageable
    );

    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId ORDER BY r.createdAt DESC")
    List<Review> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT r FROM Review r WHERE r.status.statusName = :statusName ORDER BY r.createdAt DESC")
    Page<Review> findByStatusName(@Param("statusName") String statusName, Pageable pageable);

    @Query("SELECT AVG(r.rating) FROM Review r " +
           "WHERE r.professor.professorId = :professorId " +
           "AND r.status.statusName = 'Approved'")
    Double getAverageRatingByProfessor(@Param("professorId") Integer professorId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.status.statusName = 'Approved'")
    Long countApprovedReviews();

    boolean existsByProfessorProfessorId(Integer professorId);

    boolean existsBySubjectSubjectId(Integer subjectId);

    List<Review> findByProfessorProfessorIdAndStatus_StatusName(Integer professorId, String statusName);

    Long countByProfessorProfessorIdAndStatus_StatusName(Integer professorId, String statusName);

    @Query("SELECT AVG(r.rating) FROM Review r " +
           "WHERE r.professor.professorId = :professorId " +
           "AND r.status.statusName = 'Approved'")
    Double calculateAverageRatingByProfessorId(@Param("professorId") Integer professorId);

    @Query("SELECT COUNT(v) FROM ReviewVote v " +
           "WHERE v.review.reviewId = :reviewId " +
           "AND v.voteType.voteName = 'Like'")
    Long countLikesByReviewId(@Param("reviewId") Integer reviewId);

    @Query("SELECT COUNT(v) FROM ReviewVote v " +
           "WHERE v.review.reviewId = :reviewId " +
           "AND v.voteType.voteName = 'Dislike'")
    Long countDislikesByReviewId(@Param("reviewId") Integer reviewId);

    // Método para buscar reseñas por userId (Long)
    List<Review> findByUser_UserId(Long userId);

    // Método para buscar reseña por reviewId (Long)
    @Query("SELECT r FROM Review r WHERE r.reviewId = :reviewId")
    java.util.Optional<Review> findById(@Param("reviewId") Long reviewId);
}
