package com.fime.ratemyprofs.model.dto.professor;

import com.fime.ratemyprofs.model.dto.review.ReviewResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorResponse {

    private Integer professorId;
    private String name;
    private String title;
    private String departmentName;
    private String photoUrl;
    private Double averageRating;
    private Long reviewCount;
    private List<String> universities;
    private List<String> subjects;
    private List<ReviewResponse> reviews;
}
