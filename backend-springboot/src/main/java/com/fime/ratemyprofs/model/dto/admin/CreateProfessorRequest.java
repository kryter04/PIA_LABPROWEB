package com.fime.ratemyprofs.model.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProfessorRequest {
    
    @NotBlank(message = "Professor name is required")
    private String name;
    
    private String title;
    
    private String departmentName;
    
    private String photoUrl;
    
    @NotNull(message = "At least one university is required")
    private List<Integer> universityIds;
    
    private List<Integer> subjectIds;
}
