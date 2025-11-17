package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.BadRequestException;
import com.fime.ratemyprofs.exception.DuplicateResourceException;
import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.admin.CreateUserRequest;
import com.fime.ratemyprofs.model.dto.admin.UpdateRoleRequest;
import com.fime.ratemyprofs.model.dto.user.UserResponse;
import com.fime.ratemyprofs.model.entity.Role;
import com.fime.ratemyprofs.model.entity.User;
import com.fime.ratemyprofs.repository.RoleRepository;
import com.fime.ratemyprofs.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Obtiene todos los usuarios con paginación
     * Solo para administradores
     */
    public Page<UserResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> users = userRepository.findAll(pageable);
        
        return users.map(this::mapToUserResponse);
    }

    /**
     * Crea un nuevo usuario manualmente
     * Solo para administradores
     * Permite asignar cualquier rol al crear el usuario
     */
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        // Validar que el rol existe y es válido
        String roleName = request.getRoleName();
        if (!roleName.equals("Admin") && !roleName.equals("Estudiante")) {
            throw new BadRequestException("Rol inválido. Debe ser 'Admin' o 'Estudiante'");
        }

        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol no encontrado: " + roleName));

        // Crear el nuevo usuario
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        
        return mapToUserResponse(savedUser);
    }

    /**
     * Obtiene un usuario específico por ID
     * Solo para administradores
     */
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + userId));
        
        return mapToUserResponse(user);
    }

    /**
     * Actualiza el rol de un usuario
     * Solo para administradores
     */
    @Transactional
    public UserResponse updateUserRole(Long userId, UpdateRoleRequest request) {
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + userId));

        // Validar que el rol existe
        String roleName = request.getRoleName();
        if (!roleName.equals("Admin") && !roleName.equals("Estudiante")) {
            throw new BadRequestException("Rol inválido. Debe ser 'Admin' o 'Estudiante'");
        }

        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol no encontrado: " + roleName));

        user.setRole(role);
        User updatedUser = userRepository.save(user);
        
        return mapToUserResponse(updatedUser);
    }

    /**
     * Elimina un usuario del sistema
     * Solo para administradores
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + userId));

        // Opcional: Prevenir que el administrador se elimine a sí mismo
        // Esta validación puede hacerse en el controller verificando el userId autenticado

        userRepository.delete(user);
    }

    /**
     * Helper method para mapear User a UserResponse
     */
    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().getRoleName())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
