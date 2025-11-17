package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.dto.admin.CreateSubjectRequest;
import com.fime.ratemyprofs.model.dto.admin.UpdateSubjectRequest;
import com.fime.ratemyprofs.model.entity.Subject;
import com.fime.ratemyprofs.service.AdminSubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/subjects")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSubjectController {

    private final AdminSubjectService adminSubjectService;

    @PostMapping
    public ResponseEntity<Subject> createSubject(@Valid @RequestBody CreateSubjectRequest request) {
        Subject subject = adminSubjectService.createSubject(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(subject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSubjectRequest request) {
        Subject subject = adminSubjectService.updateSubject(id, request);
        return ResponseEntity.ok(subject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        adminSubjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}
