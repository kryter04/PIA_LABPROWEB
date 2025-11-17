package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.BadRequestException;
import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.admin.CreateSubjectRequest;
import com.fime.ratemyprofs.model.dto.admin.UpdateSubjectRequest;
import com.fime.ratemyprofs.model.entity.Subject;
import com.fime.ratemyprofs.repository.ReviewRepository;
import com.fime.ratemyprofs.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminSubjectService {

    private final SubjectRepository subjectRepository;
    private final ReviewRepository reviewRepository;

    @Transactional
    public Subject createSubject(CreateSubjectRequest request) {
        Subject subject = Subject.builder()
                .name(request.getName())
                .build();

        return subjectRepository.save(subject);
    }

    @Transactional
    public Subject updateSubject(Long subjectId, UpdateSubjectRequest request) {
        Subject subject = subjectRepository.findById(subjectId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Materia no encontrada con ID: " + subjectId));

        if (request.getName() != null && !request.getName().isBlank()) {
            subject.setName(request.getName());
        }

        return subjectRepository.save(subject);
    }

    @Transactional
    public void deleteSubject(Long subjectId) {
        Subject subject = subjectRepository.findById(subjectId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Materia no encontrada con ID: " + subjectId));

        // Verificar que no tenga reseñas
        if (reviewRepository.existsBySubjectSubjectId(subjectId.intValue())) {
            throw new BadRequestException(
                    "No se puede eliminar la materia porque tiene reseñas asociadas");
        }

        subjectRepository.delete(subject);
    }
}
