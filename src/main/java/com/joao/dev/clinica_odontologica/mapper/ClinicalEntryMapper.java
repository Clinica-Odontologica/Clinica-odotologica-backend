package com.joao.dev.clinica_odontologica.mapper;

import com.joao.dev.clinica_odontologica.dto.clinical.ClinicalEntryRequestDTO;
import com.joao.dev.clinica_odontologica.dto.clinical.ClinicalEntryResponseDTO;
import com.joao.dev.clinica_odontologica.entity.ClinicalEntry;
import org.springframework.stereotype.Component;

@Component
public class ClinicalEntryMapper {

    public static ClinicalEntryResponseDTO toDTO(ClinicalEntry entry) {
        if (entry == null) return null;
        ClinicalEntryResponseDTO dto = new ClinicalEntryResponseDTO();
        dto.setTurnId(entry.getId());
        dto.setDiagnosis(entry.getDiagnosis());
        dto.setTreatmentNotes(entry.getTreatmentNotes());
        dto.setCreatedAt(entry.getCreatedAt());
        return dto;
    }

    public static ClinicalEntry toEntity(ClinicalEntryRequestDTO dto) {
        if (dto == null) return null;

        ClinicalEntry entry = new ClinicalEntry();
        entry.setDiagnosis(dto.getDiagnosis());
        entry.setTreatmentNotes(dto.getTreatmentNotes());
        entry.setAttachmentUrl(dto.getAttachmentUrl());

        return entry;
    }
}