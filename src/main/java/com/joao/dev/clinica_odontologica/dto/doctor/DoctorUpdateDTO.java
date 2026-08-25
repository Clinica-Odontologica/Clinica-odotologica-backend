package com.joao.dev.clinica_odontologica.dto.doctor;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DoctorUpdateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotBlank(message = "La especialidad es obligatoria")
    private String specialty;

    private Boolean isActive;
}
