package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.model.entity.University;
import com.fime.ratemyprofs.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;

    /**
     * Obtiene la lista completa de universidades
     */
    public List<University> getAllUniversities() {
        return universityRepository.findAll();
    }
}
