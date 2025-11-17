package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.DuplicateResourceException;
import com.fime.ratemyprofs.exception.BadRequestException;
import com.fime.ratemyprofs.model.dto.auth.AuthResponse;
import com.fime.ratemyprofs.model.dto.auth.LoginRequest;
import com.fime.ratemyprofs.model.dto.auth.RegisterRequest;
import com.fime.ratemyprofs.model.entity.Role;
import com.fime.ratemyprofs.model.entity.User;
import com.fime.ratemyprofs.repository.RoleRepository;
import com.fime.ratemyprofs.repository.UserRepository;
import com.fime.ratemyprofs.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists");
        }

        // Obtener el rol de Estudiante (roleId = 2)
        Role studentRole = roleRepository.findById(2)
                .orElseThrow(() -> new BadRequestException("Student role not found"));

        // Crear nuevo usuario
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .role(studentRole)
                .createdAt(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        // Generar JWT token (usando email como username)
        String token = jwtService.generateToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPasswordHash())
                        .authorities("ROLE_" + user.getRole().getRoleName().toUpperCase())
                        .build()
        );

        return new AuthResponse(
                token,
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole().getRoleName()
        );
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            // Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Obtener usuario autenticado
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadRequestException("Invalid email or password"));

            // Generar JWT token (usando email como username)
            String token = jwtService.generateToken(
                    org.springframework.security.core.userdetails.User.builder()
                            .username(user.getEmail())
                            .password(user.getPasswordHash())
                            .authorities("ROLE_" + user.getRole().getRoleName().toUpperCase())
                            .build()
            );

            return new AuthResponse(
                    token,
                    user.getUserId(),
                    user.getName(),
                    user.getEmail(),
                    user.getRole().getRoleName()
            );

        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid email or password");
        }
    }
}
