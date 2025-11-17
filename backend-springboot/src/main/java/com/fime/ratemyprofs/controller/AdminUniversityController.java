package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.entity.University;
import com.fime.ratemyprofs.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/universities")
@RequiredArgsConstructor
public class AdminUniversityController {

    private final UniversityService universityService;

    /**
     * POST /api/admin/universities
     * Crea una nueva universidad
     * Requiere rol: ADMIN
     */
    @PostMapping
    public ResponseEntity<University> createUniversity(@Valid @RequestBody University university) {
        University created = universityService.createUniversity(university);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/admin/universities/{id}
     * Actualiza una universidad existente
     * Requiere rol: ADMIN
     */
    @PutMapping("/{id}")
    public ResponseEntity<University> updateUniversity(
            @PathVariable Integer id,
            @Valid @RequestBody University university) {
        University updated = universityService.updateUniversity(id, university);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/admin/universities/{id}
     * Elimina una universidad
     * Requiere rol: ADMIN
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Integer id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}
