package com.joao.dev.clinica_odontologica.dto.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoctorRequestDTO {
    // Datos del Doctor
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotBlank(message = "La especialidad es obligatoria")
    private String specialty;

    // Datos para crear su Usuario de Login automáticamente
    @NotBlank(message = "El username es obligatorio")
    private String username;

    @NotBlank(message = "El password es obligatorio")
    private String password;

    @Email(message = "Email inválido")
    private String email;
}