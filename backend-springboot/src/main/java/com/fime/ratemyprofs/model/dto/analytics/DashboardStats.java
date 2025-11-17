package com.fime.ratemyprofs.model.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Long totalUsers;
    private Long totalProfessors;
    private Long totalReviews;
    private Long pendingReviews;
    private Double averageRating;
}
