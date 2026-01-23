package com.joao.dev.clinica_odontologica.dto.auth;

import com.joao.dev.clinica_odontologica.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    private Long id;
    private String username;
    private String fullname;
    private String email;
    private boolean isActive;
    private Role rol;
    private String accessToken;
    private String refreshToken;
}
