package com.joao.dev.clinica_odontologica.mapper;

import com.joao.dev.clinica_odontologica.dto.medical.MedicalHistoryDTO;
import com.joao.dev.clinica_odontologica.entity.MedicalHistoryEntry;
import org.springframework.stereotype.Component;

@Component
public class MedicalHistoryMapper {

    public static MedicalHistoryDTO toDTO(MedicalHistoryEntry entry) {
        if (entry == null) return null;

        MedicalHistoryDTO dto = new MedicalHistoryDTO();
        dto.setId(entry.getId());
        dto.setDescription(entry.getDescription());
        dto.setCreatedAt(entry.getCreatedAt());

        if (entry.getCreatedByUser() != null) {
            dto.setCreatedByUserName(entry.getCreatedByUser().getFullName());
        }

        return dto;
    }

}