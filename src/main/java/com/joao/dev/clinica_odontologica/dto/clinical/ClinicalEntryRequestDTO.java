package com.joao.dev.clinica_odontologica.dto.clinical;

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
public class ClinicalEntryRequestDTO {
    @NotNull(message = "El ID del turno es obligatorio")
    private Long turnId;

    @NotBlank(message = "El diagnóstico es obligatorio")
    private String diagnosis;

    @NotBlank(message = "Las notas del tratamiento son obligatorias")
    private String treatmentNotes;

    private String attachmentUrl;
}
