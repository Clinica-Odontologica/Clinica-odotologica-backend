package com.joao.dev.clinica_odontologica.dto.turn;

import com.joao.dev.clinica_odontologica.dto.service.ServiceDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TurnResponseDTO {
    private Long id;

    private Long patientId;
    private String patientName;
    private String patientDni;

    private Long doctorId;
    private String doctorName;
    private String doctorSpecialty;

    private String status;
    private LocalDateTime appointmentDate;

    private List<ServiceDTO> services;
    private BigDecimal totalCost;
}