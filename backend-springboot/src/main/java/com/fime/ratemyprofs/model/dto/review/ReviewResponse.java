package com.fime.ratemyprofs.model.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private Integer reviewId;
    private Integer userId;
    private String userName;
    private Integer professorId;
    private String professorName;
    private Integer subjectId;
    private String subjectName;
    private Short rating;
    private String comment;
    private Boolean isAnonymous;
    private String status;
    private LocalDateTime createdAt;
    private List<String> imageUrls;
    private Integer totalVotes;
    private Long likeCount;
    private Long dislikeCount;
}
