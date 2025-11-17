package com.fime.ratemyprofs.model.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponse {

    private String message;
    private Integer reviewId;
    private String voteType;
    private Integer totalVotes;
}
