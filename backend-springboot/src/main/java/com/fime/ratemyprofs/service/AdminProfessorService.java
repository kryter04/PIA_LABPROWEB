package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.BadRequestException;
import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.admin.CreateProfessorRequest;
import com.fime.ratemyprofs.model.dto.admin.UpdateProfessorRequest;
import com.fime.ratemyprofs.model.dto.professor.ProfessorResponse;
import com.fime.ratemyprofs.model.entity.Professor;
import com.fime.ratemyprofs.model.entity.University;
import com.fime.ratemyprofs.repository.ProfessorRepository;
import com.fime.ratemyprofs.repository.ReviewRepository;
import com.fime.ratemyprofs.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminProfessorService {

    private final ProfessorRepository professorRepository;
    private final UniversityRepository universityRepository;
    private final ReviewRepository reviewRepository;

    /**
     * Crea un nuevo profesor
     * Solo para administradores
     */
    @Transactional
    public ProfessorResponse createProfessor(CreateProfessorRequest request) {
        // Verificar que la universidad existe
        University university = universityRepository.findById(request.getUniversityId().intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Universidad no encontrada con ID: " + request.getUniversityId()));

        Professor professor = Professor.builder()
                .name(request.getName())
                .build();
        
        professor.getUniversities().add(university);

        Professor savedProfessor = professorRepository.save(professor);
        
        return mapToProfessorResponse(savedProfessor);
    }

    /**
     * Actualiza un profesor existente
     * Solo para administradores
     */
    @Transactional
    public ProfessorResponse updateProfessor(Long professorId, UpdateProfessorRequest request) {
        Professor professor = professorRepository.findById(professorId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profesor no encontrado con ID: " + professorId));

        // Actualizar nombre si se proporciona
        if (request.getName() != null && !request.getName().isBlank()) {
            professor.setName(request.getName());
        }

        // Actualizar universidad si se proporciona
        if (request.getUniversityId() != null) {
            University university = universityRepository.findById(request.getUniversityId().intValue())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Universidad no encontrada con ID: " + request.getUniversityId()));
            professor.getUniversities().clear();
            professor.getUniversities().add(university);
        }

        Professor updatedProfessor = professorRepository.save(professor);
        
        return mapToProfessorResponse(updatedProfessor);
    }

    /**
     * Elimina un profesor del sistema
     * Solo para administradores
     * Valida que no tenga reseñas asociadas
     */
    @Transactional
    public void deleteProfessor(Long professorId) {
        Professor professor = professorRepository.findById(professorId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profesor no encontrado con ID: " + professorId));

        // Verificar que no tenga reseñas
        if (reviewRepository.existsByProfessorProfessorId(professorId.intValue())) {
            throw new BadRequestException(
                    "No se puede eliminar el profesor porque tiene reseñas asociadas");
        }

        professorRepository.delete(professor);
    }

    /**
     * Helper method para mapear Professor a ProfessorResponse
     */
    private ProfessorResponse mapToProfessorResponse(Professor professor) {
        return ProfessorResponse.builder()
                .professorId(professor.getProfessorId())
                .name(professor.getName())
                .title(professor.getTitle())
                .departmentName(professor.getDepartmentName())
                .photoUrl(professor.getPhotoUrl())
                .averageRating(0.0)
                .reviewCount(0L)
                .universities(professor.getUniversities().stream()
                        .map(University::getName)
                        .collect(java.util.stream.Collectors.toList()))
                .subjects(professor.getSubjects().stream()
                        .map(Subject::getName)
                        .collect(java.util.stream.Collectors.toList()))
                .build();
    }
}
