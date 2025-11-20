package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.model.dto.analytics.DashboardStats;
import com.fime.ratemyprofs.model.dto.professor.ProfessorResponse;
import com.fime.ratemyprofs.repository.ProfessorRepository;
import com.fime.ratemyprofs.repository.ReviewRepository;
import com.fime.ratemyprofs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;
    private final ReviewRepository reviewRepository;
    private final ProfessorService professorService;

    public DashboardStats getDashboardStats() {
        Long totalUsers = userRepository.count();
        Long totalProfessors = professorRepository.count();
        Long totalReviews = reviewRepository.count();
        
        Long pendingReviews = reviewRepository
                .findByStatusName("Pending", PageRequest.of(0, 1))
                .getTotalElements();
        
        Long approvedReviews = reviewRepository.countApprovedReviews();
        
        Long rejectedReviews = reviewRepository
                .findByStatusName("Rejected", PageRequest.of(0, 1))
                .getTotalElements();

        ProfessorResponse topRatedProfessor = getTopRatedProfessor();

        return DashboardStats.builder()
                .activeUsers(totalUsers)
                .totalUsers(totalUsers)
                .totalProfessors(totalProfessors)
                .totalReviews(totalReviews)
                .approvedReviews(approvedReviews)
                .pendingReviews(pendingReviews)
                .rejectedReviews(rejectedReviews)
                .averageRating(4.0)
                .topRatedProfessor(topRatedProfessor)
                .build();
    }

    private ProfessorResponse getTopRatedProfessor() {
        List<ProfessorResponse> ranking = professorService.getRanking(1);
        return ranking.isEmpty() ? null : ranking.get(0);
    }
}
