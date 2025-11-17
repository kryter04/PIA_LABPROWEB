package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.dto.professor.ProfessorResponse;
import com.fime.ratemyprofs.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    /**
     * GET /api/professors
     * Lista todos los profesores con filtros opcionales
     * Query params:
     * - name: Filtrar por nombre (búsqueda parcial)
     * - universityId: Filtrar por universidad
     * - subjectId: Filtrar por materia
     * - page: Número de página (default: 0)
     * - size: Tamaño de página (default: 10)
     */
    @GetMapping("/professors")
    public ResponseEntity<Page<ProfessorResponse>> getAllProfessors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer universityId,
            @RequestParam(required = false) Integer subjectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<ProfessorResponse> professors = professorService.getAllProfessors(
            name, universityId, subjectId, page, size
        );
        return ResponseEntity.ok(professors);
    }

    /**
     * GET /api/professors/{id}
     * Obtiene los detalles de un profesor específico
     * Incluye todas las reseñas aprobadas del profesor
     */
    @GetMapping("/professors/{id}")
    public ResponseEntity<ProfessorResponse> getProfessorById(@PathVariable Integer id) {
        ProfessorResponse professor = professorService.getProfessorById(id);
        return ResponseEntity.ok(professor);
    }

    /**
     * GET /api/ranking
     * Obtiene el ranking de los mejores profesores
     * Query params:
     * - limit: Número de profesores a retornar (default: 10, max: 100)
     */
    @GetMapping("/ranking")
    public ResponseEntity<List<ProfessorResponse>> getRanking(
            @RequestParam(defaultValue = "10") int limit) {
        
        // Validar que el límite no exceda 100
        if (limit > 100) {
            limit = 100;
        }
        
        List<ProfessorResponse> ranking = professorService.getRanking(limit);
        return ResponseEntity.ok(ranking);
    }
}
