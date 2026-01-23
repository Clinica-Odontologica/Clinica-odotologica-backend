package com.joao.dev.clinica_odontologica.service;

import com.joao.dev.clinica_odontologica.dto.auth.*;
import com.joao.dev.clinica_odontologica.entity.User;
import com.joao.dev.clinica_odontologica.exceptions.ResourceNotFoundException;
import com.joao.dev.clinica_odontologica.mapper.AuthMapper;
import com.joao.dev.clinica_odontologica.repository.UserRepository;
import com.joao.dev.clinica_odontologica.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional(readOnly = true)
    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new RuntimeException("El email " + registerRequestDTO.getEmail() + " ya está en uso.");
        }
        if (userRepository.existsByUsername(registerRequestDTO.getUsername())){
            throw new RuntimeException("El nombre de usuario " + registerRequestDTO.getUsername() + " ya está en uso.");
        }
        User user = AuthMapper.toEntity(registerRequestDTO);

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(),user.getRole().getName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        user.setPassword(passwordEncoder.encode((registerRequestDTO.getPasswordd())));
        User savedUser = userRepository.save(user);
        RegisterResponseDTO response = AuthMapper.registerToDTO(savedUser);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        String email = loginRequestDTO.getEmail();
        String passwordd = loginRequestDTO.getPasswordd();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el email: " + email));

        if (!passwordEncoder.matches(passwordd,user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta, intente de nuevo.");
        }

        String accessToken = jwtUtil.generateAccessToken(user.getEmail(),user.getRole().getName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        LoginResponseDTO response = AuthMapper.loginToDTO(user);
        response.setAccessToken(accessToken);
        response.setRefreshToken(refreshToken);

        return response;
    }

    public RefreshTokenResponseDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        String refreshToken = refreshTokenRequestDTO.getRefreshToken();

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token inválido o expirado.");
        }

        String email = jwtUtil.getEmailFromToken(refreshToken); // O getEmailFromToken según como lo hayas llamado

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado asociada al token"));

        if (!user.getIsActive()) {
            throw new RuntimeException("El usuario está inactivo, no se puede refrescar el token.");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().getName());

        return RefreshTokenResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
