package com.fime.ratemyprofs.controller;

import com.fime.ratemyprofs.model.dto.admin.CreateUserRequest;
import com.fime.ratemyprofs.model.dto.admin.UpdateRoleRequest;
import com.fime.ratemyprofs.model.dto.user.UserResponse;
import com.fime.ratemyprofs.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * GET /api/admin/users
     * Lista todos los usuarios del sistema (solo administradores)
     * Query params:
     * - page: Número de página (default: 0)
     * - size: Tamaño de página (default: 10)
     */
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Page<UserResponse> users = adminUserService.getAllUsers(page, size);
        return ResponseEntity.ok(users);
    }

    /**
     * POST /api/admin/users
     * Crea un nuevo usuario manualmente (solo administradores)
     * Body: {
     *   "name": "string",
     *   "email": "string",
     *   "password": "string",
     *   "roleName": "Admin" | "Estudiante"
     * }
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        
        UserResponse newUser = adminUserService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    /**
     * GET /api/admin/users/{userId}
     * Obtiene los detalles de un usuario específico (solo administradores)
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        UserResponse user = adminUserService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * PUT /api/admin/users/{userId}/role
     * Actualiza el rol de un usuario (solo administradores)
     * Body: { "roleName": "Admin" | "Estudiante" }
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<UserResponse> updateUserRole(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRoleRequest request) {
        
        UserResponse updatedUser = adminUserService.updateUserRole(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * DELETE /api/admin/users/{userId}
     * Elimina un usuario del sistema (solo administradores)
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminUserService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
