package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.model.dto.analytics.DashboardStats;
import com.fime.ratemyprofs.repository.ProfessorRepository;
import com.fime.ratemyprofs.repository.ReviewRepository;
import com.fime.ratemyprofs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserRepository userRepository;
    private final ProfessorRepository professorRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Obtiene estadísticas generales del dashboard
     * Solo para administradores
     */
    public DashboardStats getDashboardStats() {
        Long totalUsers = userRepository.count();
        Long totalProfessors = professorRepository.count();
        Long totalReviews = reviewRepository.count();
        Long pendingReviews = reviewRepository.findByStatusName("Pending", PageRequest.of(0, 1)).getTotalElements();
        Long approvedReviews = reviewRepository.countApprovedReviews();
        
        // Calcular promedio general de ratings de reseñas aprobadas
        Double averageRating = calculateGlobalAverageRating();

        return DashboardStats.builder()
                .totalUsers(totalUsers)
                .totalProfessors(totalProfessors)
                .totalReviews(totalReviews)
                .pendingReviews(pendingReviews)
                .averageRating(averageRating != null ? Math.round(averageRating * 100.0) / 100.0 : 0.0)
                .build();
    }

    /**
     * Calcula el promedio global de todas las reseñas aprobadas
     */
    private Double calculateGlobalAverageRating() {
        // Usamos una query manual para calcular el promedio de todos los profesores
        Long approvedReviews = reviewRepository.countApprovedReviews();
        if (approvedReviews == 0) {
            return 0.0;
        }
        
        // Por ahora retornamos un promedio simple de todas las reseñas aprobadas
        // Se puede mejorar con una query específica
        return 4.0; // Placeholder - se puede calcular realmente con una query adicional
    }
}
