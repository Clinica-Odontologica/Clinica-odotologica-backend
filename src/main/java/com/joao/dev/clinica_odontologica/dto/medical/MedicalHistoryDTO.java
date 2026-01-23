package com.joao.dev.clinica_odontologica.dto.medical;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicalHistoryDTO {
    private Long id;
    private String description;
    private String createdByUserName;
    private LocalDateTime createdAt;
}
