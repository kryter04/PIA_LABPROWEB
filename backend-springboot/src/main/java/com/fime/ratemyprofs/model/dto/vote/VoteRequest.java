package com.fime.ratemyprofs.model.dto.vote;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    @NotBlank(message = "Vote type is required")
    @Pattern(regexp = "Like|Dislike", message = "Vote type must be 'Like' or 'Dislike'")
    private String voteType;
}
