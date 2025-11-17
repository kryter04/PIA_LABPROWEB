package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.dto.admin.CreateProfessorRequest;
import com.fime.ratemyprofs.model.dto.admin.UpdateProfessorRequest;
import com.fime.ratemyprofs.model.dto.professor.ProfessorResponse;
import com.fime.ratemyprofs.service.AdminProfessorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/professors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProfessorController {

    private final AdminProfessorService adminProfessorService;

    /**
     * POST /api/admin/professors
     * Crea un nuevo profesor (solo administradores)
     * Body: { "name": "...", "universityId": 1 }
     */
    @PostMapping
    public ResponseEntity<ProfessorResponse> createProfessor(
            @Valid @RequestBody CreateProfessorRequest request) {
        
        ProfessorResponse professor = adminProfessorService.createProfessor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(professor);
    }

    /**
     * PUT /api/admin/professors/{id}
     * Actualiza un profesor existente (solo administradores)
     * Body: { "name": "...", "universityId": 1 }
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfessorResponse> updateProfessor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfessorRequest request) {
        
        ProfessorResponse professor = adminProfessorService.updateProfessor(id, request);
        return ResponseEntity.ok(professor);
    }

    /**
     * DELETE /api/admin/professors/{id}
     * Elimina un profesor del sistema (solo administradores)
     * Solo si no tiene reseñas asociadas
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfessor(@PathVariable Long id) {
        adminProfessorService.deleteProfessor(id);
        return ResponseEntity.noContent().build();
    }
}
