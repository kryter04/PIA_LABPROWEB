package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.professor.ProfessorResponse;
import com.fime.ratemyprofs.model.dto.review.ReviewResponse;
import com.fime.ratemyprofs.model.entity.Professor;
import com.fime.ratemyprofs.model.entity.Review;
import com.fime.ratemyprofs.repository.ProfessorRepository;
import com.fime.ratemyprofs.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;
    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Page<ProfessorResponse> getAllProfessors(String name, Integer universityId, Integer subjectId, 
                                                  int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Professor> professorPage;

        // Aplicar filtros según los parámetros
        if (name != null && !name.trim().isEmpty()) {
            professorPage = professorRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            professorPage = professorRepository.findAll(pageable);
        }

        // Convertir a Page<ProfessorResponse>
        return professorPage.map(this::mapToProfessorResponse);
    }

    @Transactional(readOnly = true)
    public ProfessorResponse getProfessorById(Integer id) {
        Professor professor = professorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + id));

        return mapToProfessorResponse(professor);
    }

    @Transactional(readOnly = true)
    public List<ProfessorResponse> getRanking(int limit) {
        List<Professor> professors = professorRepository.findAll().stream()
                .filter(p -> {
                    Double avg = calculateAverageRating(p.getProfessorId());
                    return avg != null && avg > 0;
                })
                .sorted((p1, p2) -> {
                    Double avg1 = calculateAverageRating(p1.getProfessorId());
                    Double avg2 = calculateAverageRating(p2.getProfessorId());
                    return Double.compare(avg2, avg1); // Descendente
                })
                .limit(limit)
                .collect(Collectors.toList());

        return professors.stream()
                .map(this::mapToProfessorResponse)
                .collect(Collectors.toList());
    }

    private ProfessorResponse mapToProfessorResponse(Professor professor) {
        return ProfessorResponse.builder()
                .professorId(professor.getProfessorId())
                .name(professor.getName())
                .title(professor.getTitle())
                .departmentName(professor.getDepartmentName())
                .photoUrl(professor.getPhotoUrl())
                .averageRating(calculateAverageRating(professor.getProfessorId()))
                .reviewCount(countApprovedReviews(professor.getProfessorId()))
                .universities(professor.getUniversities().stream()
                        .map(u -> u.getName())
                        .collect(Collectors.toList()))
                .subjects(professor.getSubjects().stream()
                        .map(s -> s.getName())
                        .collect(Collectors.toList()))
                .build();
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        Long likeCount = reviewRepository.countLikesByReviewId(review.getReviewId());
        Long dislikeCount = reviewRepository.countDislikesByReviewId(review.getReviewId());

        return ReviewResponse.builder()
                .reviewId(review.getReviewId())
                .userId(review.getIsAnonymous() ? null : (review.getUser() != null ? review.getUser().getUserId() : null))
                .userName(review.getIsAnonymous() ? "Anonymous" : 
                         (review.getUser() != null ? review.getUser().getName() : "Unknown"))
                .professorId(review.getProfessor().getProfessorId())
                .professorName(review.getProfessor().getName())
                .subjectId(review.getSubject().getSubjectId())
                .subjectName(review.getSubject().getName())
                .rating(review.getRating())
                .comment(review.getComment())
                .isAnonymous(review.getIsAnonymous())
                .status(review.getStatus().getStatusName())
                .createdAt(review.getCreatedAt())
                .imageUrls(review.getImages().stream()
                        .map(img -> img.getImageUrl())
                        .collect(Collectors.toList()))
                .totalVotes((int) (likeCount - dislikeCount))
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .build();
    }

    private Double calculateAverageRating(Integer professorId) {
        Double avg = reviewRepository.calculateAverageRatingByProfessorId(professorId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : null;
    }

    private Long countApprovedReviews(Integer professorId) {
        return reviewRepository.countByProfessorProfessorIdAndStatus_StatusName(professorId, "Approved");
    }
}
