package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.model.entity.Subject;
import com.fime.ratemyprofs.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    /**
     * Obtiene la lista completa de materias
     */
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }
}
