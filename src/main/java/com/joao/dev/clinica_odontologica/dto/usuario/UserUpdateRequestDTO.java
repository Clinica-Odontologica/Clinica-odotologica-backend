package com.joao.dev.clinica_odontologica.dto.usuario;

import com.joao.dev.clinica_odontologica.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDTO {

    @NotBlank(message = "username field cannot be null")
    private String username;

    @NotBlank(message = "password field cannot be null")
    private String password;

    @NotBlank(message = "nombre field cannot be null")
    private String fullname;

    @NotBlank(message = "email field cannot be null")
    @Email(message = "email field must be a valid email address")
    private String email;

    @NotNull(message = "rol field cannot be null")
    private Role rol;

    @NotNull(message = "isActive field cannot be null")
    private Boolean isActive;
}
