package com.fime.ratemyprofs.service;

import com.fime.ratemyprofs.exception.DuplicateResourceException;
import com.fime.ratemyprofs.exception.BadRequestException;
import com.fime.ratemyprofs.exception.ResourceNotFoundException;
import com.fime.ratemyprofs.model.dto.auth.AuthResponse;
import com.fime.ratemyprofs.model.dto.auth.LoginRequest;
import com.fime.ratemyprofs.model.dto.auth.PasswordRecoveryRequest;
import com.fime.ratemyprofs.model.dto.auth.PasswordRecoveryResponse;
import com.fime.ratemyprofs.model.dto.auth.PasswordResetRequest;
import com.fime.ratemyprofs.model.dto.auth.RegisterRequest;
import com.fime.ratemyprofs.model.entity.PasswordRecoveryToken;
import com.fime.ratemyprofs.model.entity.Role;
import com.fime.ratemyprofs.model.entity.User;
import com.fime.ratemyprofs.repository.PasswordRecoveryTokenRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordRecoveryTokenRepository tokenRepository;
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

    /**
     * Inicia el proceso de recuperación de contraseña
     * Genera un token temporal y lo guarda en BD con expiración de 1 hora
     * NO retorna el token en la respuesta (seguridad)
     * 
     * @param request Contiene el email del usuario
     * @return Respuesta con mensaje genérico
     */
    @Transactional
    public PasswordRecoveryResponse initiatePasswordRecovery(PasswordRecoveryRequest request) {
        // Verificar que el usuario existe
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No user found with email: " + request.getEmail()));

        // Invalidar todos los tokens previos del usuario
        tokenRepository.invalidateAllUserTokens(user.getUserId());

        // Generar token único (UUID)
        String token = UUID.randomUUID().toString();

        // Crear token de recuperación con expiración de 1 hora
        PasswordRecoveryToken recoveryToken = PasswordRecoveryToken.builder()
                .user(user)
                .token(token)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();

        tokenRepository.save(recoveryToken);

        // En producción, aquí se enviaría el email con el link:
        // emailService.sendPasswordRecoveryEmail(user.getEmail(), token);

        // NO retornar el token en la respuesta (seguridad)
        return PasswordRecoveryResponse.builder()
                .message("If your email exists in our system, you will receive password recovery instructions")
                .email(request.getEmail())
                .recoveryToken(null) // NUNCA retornar el token
                .build();
    }

    /**
     * Resetea la contraseña usando un token válido
     * Valida que el token exista, no esté usado y no esté expirado
     * 
     * @param request Contiene el token y la nueva contraseña
     * @return Mensaje de éxito
     */
    @Transactional
    public PasswordRecoveryResponse resetPassword(PasswordResetRequest request) {
        // Buscar token válido (no usado y no expirado)
        PasswordRecoveryToken token = tokenRepository.findValidToken(
                request.getToken(), 
                LocalDateTime.now()
        ).orElseThrow(() -> new BadRequestException(
                "Invalid or expired recovery token"));

        // Obtener el usuario
        User user = token.getUser();

        // Actualizar la contraseña
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Marcar el token como usado
        token.setUsed(true);
        tokenRepository.save(token);

        // Invalidar cualquier otro token del usuario
        tokenRepository.invalidateAllUserTokens(user.getUserId());

        return PasswordRecoveryResponse.builder()
                .message("Password successfully reset. You can now login with your new password.")
                .email(user.getEmail())
                .recoveryToken(null)
                .build();
    }
}
