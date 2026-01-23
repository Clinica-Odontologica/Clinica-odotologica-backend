package com.joao.dev.clinica_odontologica.dto.turn;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TurnRequestDTO {
    @NotNull(message = "El paciente es obligatorio")
    private Long patientId;

    @NotNull(message = "El doctor es obligatorio")
    private Long doctorId;

    @NotNull(message = "Debe seleccionar al menos un servicio")
    private List<Long> serviceIds;

    private LocalDateTime appointmentDate;
}
