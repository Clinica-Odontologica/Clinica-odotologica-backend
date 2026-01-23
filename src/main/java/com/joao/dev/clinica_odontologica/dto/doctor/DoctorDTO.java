package com.joao.dev.clinica_odontologica.dto.doctor;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDTO {
    private Long id;

    @NotBlank(message = "name field cannot be null")
    private String name;

    @NotBlank(message = "lastName field cannot be null")
    private String lastName;

    @NotBlank(message = "specialty field cannot be null")
    private String specialty;
    private Boolean isActive;
}