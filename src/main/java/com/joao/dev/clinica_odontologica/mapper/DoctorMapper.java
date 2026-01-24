package com.joao.dev.clinica_odontologica.mapper;

import com.joao.dev.clinica_odontologica.dto.doctor.DoctorDTO;
import com.joao.dev.clinica_odontologica.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public static DoctorDTO toDTO(Doctor doctor) {
        if (doctor == null) return null;

        DoctorDTO dto = new DoctorDTO();
        dto.setId(doctor.getId());
        dto.setName(doctor.getName());
        dto.setLastName(doctor.getLastName());
        dto.setSpecialty(doctor.getSpecialty());
        dto.setIsActive(doctor.getIsActive());
        return dto;
    }

    public static Doctor toEntity(DoctorDTO dto) {
        if (dto == null) return null;

        Doctor doctor = new Doctor();
        doctor.setName(dto.getName());
        doctor.setLastName(dto.getLastName());
        doctor.setSpecialty(dto.getSpecialty());
        return doctor;
    }
}