package com.joao.dev.clinica_odontologica.mapper;

import com.joao.dev.clinica_odontologica.dto.patient.PatientDTO;
import com.joao.dev.clinica_odontologica.entity.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public static PatientDTO toDTO(Patient patient) {
        if (patient == null) return null;

        PatientDTO dto = new PatientDTO();
        dto.setId(patient.getId());
        dto.setDni(patient.getDni());
        dto.setName(patient.getName());
        dto.setLast_name((patient.getLast_name()));
        dto.setPhone(patient.getPhone());
        dto.setEmail(patient.getEmail());
        return dto;
    }

    public static Patient toEntity(PatientDTO dto) {
        if (dto == null) return null;

        Patient patient = new Patient();
        if (dto.getId() != null) {
            patient.setId(dto.getId());
        }
        patient.setDni(dto.getDni());
        patient.setName(dto.getName());
        patient.setLast_name(dto.getLast_name());
        patient.setPhone(dto.getPhone());
        patient.setEmail(dto.getEmail());
        return patient;
    }
}