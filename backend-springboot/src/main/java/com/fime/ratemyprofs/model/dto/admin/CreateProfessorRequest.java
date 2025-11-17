package com.fime.ratemyprofs.model.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfessorRequest {
    
    @NotBlank(message = "El nombre del profesor es requerido")
    private String name;
    
    @NotNull(message = "El ID de la universidad es requerido")
    private Long universityId;
}
