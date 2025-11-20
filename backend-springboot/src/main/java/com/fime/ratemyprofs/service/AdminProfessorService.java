package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.BadRequestException;
import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.admin.CreateProfessorRequest;
import com.fime.ratemyprofs.model.dto.admin.UpdateProfessorRequest;
import com.fime.ratemyprofs.model.dto.professor.ProfessorResponse;
import com.fime.ratemyprofs.model.entity.Professor;
import com.fime.ratemyprofs.model.entity.Subject;
import com.fime.ratemyprofs.model.entity.University;
import com.fime.ratemyprofs.repository.ProfessorRepository;
import com.fime.ratemyprofs.repository.ReviewRepository;
import com.fime.ratemyprofs.repository.SubjectRepository;
import com.fime.ratemyprofs.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProfessorService {

    private final ProfessorRepository professorRepository;
    private final UniversityRepository universityRepository;
    private final SubjectRepository subjectRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ProfessorResponse createProfessor(CreateProfessorRequest request) {
        if (request.getUniversityIds() == null || request.getUniversityIds().isEmpty()) {
            throw new BadRequestException("At least one university is required");
        }

        List<University> universities = request.getUniversityIds().stream()
                .map(id -> universityRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("University not found with id: " + id)))
                .collect(Collectors.toList());

        List<Subject> subjects = new ArrayList<>();
        if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
            subjects = request.getSubjectIds().stream()
                    .map(id -> subjectRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id)))
                    .collect(Collectors.toList());
        }

        Professor professor = Professor.builder()
                .name(request.getName())
                .title(request.getTitle())
                .departmentName(request.getDepartmentName())
                .photoUrl(request.getPhotoUrl())
                .build();

        professor.getUniversities().addAll(universities);
        professor.getSubjects().addAll(subjects);

        Professor savedProfessor = professorRepository.save(professor);
        return mapToProfessorResponse(savedProfessor);
    }

    @Transactional
    public ProfessorResponse updateProfessor(Long professorId, UpdateProfessorRequest request) {
        Professor professor = professorRepository.findById(professorId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + professorId));

        if (request.getName() != null && !request.getName().isBlank()) {
            professor.setName(request.getName());
        }

        if (request.getTitle() != null) {
            professor.setTitle(request.getTitle());
        }

        if (request.getDepartmentName() != null) {
            professor.setDepartmentName(request.getDepartmentName());
        }

        if (request.getPhotoUrl() != null) {
            professor.setPhotoUrl(request.getPhotoUrl());
        }

        if (request.getUniversityIds() != null && !request.getUniversityIds().isEmpty()) {
            List<University> universities = request.getUniversityIds().stream()
                    .map(id -> universityRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("University not found with id: " + id)))
                    .collect(Collectors.toList());
            professor.getUniversities().clear();
            professor.getUniversities().addAll(universities);
        }

        if (request.getSubjectIds() != null) {
            if (request.getSubjectIds().isEmpty()) {
                professor.getSubjects().clear();
            } else {
                List<Subject> subjects = request.getSubjectIds().stream()
                        .map(id -> subjectRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id)))
                        .collect(Collectors.toList());
                professor.getSubjects().clear();
                professor.getSubjects().addAll(subjects);
            }
        }

        Professor updatedProfessor = professorRepository.save(professor);
        return mapToProfessorResponse(updatedProfessor);
    }

    @Transactional
    public void deleteProfessor(Long professorId) {
        Professor professor = professorRepository.findById(professorId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Professor not found with id: " + professorId));

        if (reviewRepository.existsByProfessorProfessorId(professorId.intValue())) {
            throw new BadRequestException("Cannot delete professor with existing reviews");
        }

        professorRepository.delete(professor);
    }

    private ProfessorResponse mapToProfessorResponse(Professor professor) {
        Double avgRating = reviewRepository.calculateAverageRatingByProfessorId(professor.getProfessorId());
        Long reviewCount = reviewRepository.countByProfessorProfessorIdAndStatus_StatusName(
                professor.getProfessorId(), "Approved");

        return ProfessorResponse.builder()
                .professorId(professor.getProfessorId())
                .name(professor.getName())
                .title(professor.getTitle())
                .departmentName(professor.getDepartmentName())
                .photoUrl(professor.getPhotoUrl())
                .averageRating(avgRating != null ? Math.round(avgRating * 10.0) / 10.0 : null)
                .reviewCount(reviewCount)
                .universities(professor.getUniversities().stream()
                        .map(University::getName)
                        .collect(Collectors.toList()))
                .subjects(professor.getSubjects().stream()
                        .map(Subject::getName)
                        .collect(Collectors.toList()))
                .build();
    }
}
