package com.fime.ratemyprofs.model.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfessorRequest {
    private String name;
    private String title;
    private String departmentName;
    private String photoUrl;
    private List<Integer> universityIds;
    private List<Integer> subjectIds;
}
