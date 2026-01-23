package com.joao.dev.clinica_odontologica.service;

import com.joao.dev.clinica_odontologica.dto.patient.PatientDTO;
import com.joao.dev.clinica_odontologica.entity.Patient;
import com.joao.dev.clinica_odontologica.entity.User;
import com.joao.dev.clinica_odontologica.mapper.PatientMapper;
import com.joao.dev.clinica_odontologica.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    @Transactional(readOnly = true)
    public Page<PatientDTO> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(PatientMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public PatientDTO findById(Long id) {
        return patientRepository.findById(id)
                .map(PatientMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
    }

    @Transactional(readOnly = true)
    public PatientDTO findByDni(String dni) {
        return patientRepository.findByDni(dni)
                .map(PatientMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Paciente con DNI " + dni + " no encontrado"));
    }

    @Transactional
    public PatientDTO save(PatientDTO dto) {
        if (dto.getId() == null && patientRepository.findByDni(dto.getDni()).isPresent()) {
            throw new RuntimeException("Ya existe un paciente con el DNI " + dto.getDni());
        }

        Patient entity = PatientMapper.toEntity(dto);
        Patient saved = patientRepository.save(entity);
        return PatientMapper.toDTO(saved);
    }

    @Transactional
    public void delete(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        patient.setIsActive(false);
        patientRepository.save(patient);

    }
}