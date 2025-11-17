package com.fime.ratemyprofs.model.dto.admin;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReviewRequest {

    @NotNull(message = "El rating es obligatorio")
    @Min(value = 1, message = "El rating debe ser mínimo 1")
    @Max(value = 5, message = "El rating debe ser máximo 5")
    private Short rating;

    @NotBlank(message = "El comentario es obligatorio")
    @Size(max = 1000, message = "El comentario no puede exceder 1000 caracteres")
    private String comment;
}
