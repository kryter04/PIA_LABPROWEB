package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.entity.Subject;
import com.fime.ratemyprofs.model.entity.University;
import com.fime.ratemyprofs.service.SubjectService;
import com.fime.ratemyprofs.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogController {

    private final SubjectService subjectService;
    private final UniversityService universityService;

    /**
     * GET /api/subjects
     * Obtiene la lista completa de materias disponibles
     * Endpoint público (no requiere autenticación)
     */
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getAllSubjects() {
        List<Subject> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(subjects);
    }

    /**
     * GET /api/universities
     * Obtiene la lista completa de universidades disponibles
     * Endpoint público (no requiere autenticación)
     */
    @GetMapping("/universities")
    public ResponseEntity<List<University>> getAllUniversities() {
        List<University> universities = universityService.getAllUniversities();
        return ResponseEntity.ok(universities);
    }
}
