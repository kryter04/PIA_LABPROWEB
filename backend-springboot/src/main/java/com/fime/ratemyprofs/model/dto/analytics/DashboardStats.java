package com.fime.ratemyprofs.model.dto.analytics;

import com.fime.ratemyprofs.model.dto.professor.ProfessorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Long activeUsers;
    private Long totalUsers;
    private Long totalProfessors;
    private Long totalReviews;
    private Long approvedReviews;
    private Long pendingReviews;
    private Long rejectedReviews;
    private Double averageRating;
    private ProfessorResponse topRatedProfessor;
}
