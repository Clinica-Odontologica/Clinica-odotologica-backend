package com.joao.dev.clinica_odontologica.controller;

import com.joao.dev.clinica_odontologica.config.Constant;
import com.joao.dev.clinica_odontologica.dto.GlobalResponse;
import com.joao.dev.clinica_odontologica.dto.auth.*;
import com.joao.dev.clinica_odontologica.security.JwtUtil;
import com.joao.dev.clinica_odontologica.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constant.API_VERSION + "/" + Constant.AUTH)
@Tag(name = "Autenticación", description = "Endpoints para registro de usuarios y control de acceso")
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea una nueva cuenta de usuario (Admin, Recepcionista, etc.) en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RegisterResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o usuario ya existente", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content)
    })
    public ResponseEntity<GlobalResponse<RegisterResponseDTO>> register(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        RegisterResponseDTO data = authService.register(registerRequestDTO);


        RegisterResponseDTO response = RegisterResponseDTO.builder()
                .id(data.getId())
                .fullName(data.getFullName())
                .username(data.getUsername())
                .email(data.getEmail())
                .rol(data.getRol())
                .isActive(data.getIsActive())
                .createdAt(data.getCreatedAt())
                .updatedAt(data.getUpdatedAt())
                .refreshToken(data.getRefreshToken())
                .accessToken(data.getAccessToken())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success(response, "Usuario creado exitosamente"));
    }
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica a un usuario mediante credenciales y devuelve sus datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login exitoso, credenciales válidas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciales incorrectas (Usuario o contraseña inválidos)", content = @Content),
            @ApiResponse(responseCode = "400", description = "Formato de solicitud inválido", content = @Content)
    })
    public ResponseEntity<GlobalResponse<LoginResponseDTO>> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO data = authService.login(loginRequestDTO);


        LoginResponseDTO response = LoginResponseDTO.builder()
                .id(data.getId())
                .username(data.getUsername())
                .fullname(data.getFullname())
                .email(data.getEmail())
                .isActive(data.isActive())
                .rol(data.getRol())
                .refreshToken(data.getRefreshToken())
                .accessToken(data.getAccessToken())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(response, "Login exitoso"));
    }
    @PostMapping("/refresh-token")
    @Operation(summary = "Refrescar el token de acceso",
            description = "Ubicación: Refresh Token  \n" +
                    "Seguridad: Pública(con refresh token)"
    )
    public ResponseEntity<GlobalResponse<RefreshTokenResponseDTO>> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        RefreshTokenResponseDTO data = authService.refreshToken(refreshTokenRequestDTO);

        RefreshTokenResponseDTO response = RefreshTokenResponseDTO.builder()
                .accessToken(data.getAccessToken())
                .refreshToken(data.getRefreshToken())
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(response, "Token de acceso refrescado exitosamente"));
    }

}
