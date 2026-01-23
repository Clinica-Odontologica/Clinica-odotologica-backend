package com.joao.dev.clinica_odontologica.dto.auth;

import com.joao.dev.clinica_odontologica.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequestDTO {

    @NotBlank(message = "username field cannot be null")
    private String username;

    @NotBlank(message = "fullname field cannot be null")
    @Length(min = 3, message = "fullname field must be at least 3 characters long")
    private String fullname;

    @NotBlank(message = "email field cannot be null")
    @Email(message = "email field must be a valid email address")
    private String email;

    @NotBlank(message = "passwordd field cannot be null")
    @Length(min = 8, message = "passwordd field must be at least 8 characters long")
    private String passwordd;

    @NotBlank(message = "role field cannot be null")
    private Role role;
}
