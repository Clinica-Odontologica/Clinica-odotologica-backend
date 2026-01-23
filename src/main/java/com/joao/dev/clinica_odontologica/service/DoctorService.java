package com.joao.dev.clinica_odontologica.service;

import com.joao.dev.clinica_odontologica.dto.doctor.DoctorDTO;
import com.joao.dev.clinica_odontologica.dto.doctor.DoctorRequestDTO;
import com.joao.dev.clinica_odontologica.entity.Doctor;
import com.joao.dev.clinica_odontologica.entity.Role;
import com.joao.dev.clinica_odontologica.entity.User;
import com.joao.dev.clinica_odontologica.mapper.DoctorMapper;
import com.joao.dev.clinica_odontologica.repository.DoctorRepository;
import com.joao.dev.clinica_odontologica.repository.RoleRepository;
import com.joao.dev.clinica_odontologica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public List<DoctorDTO> findAllActive() {
        return doctorRepository.findByIsActiveTrue().stream()
                .map(DoctorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<DoctorDTO> findAll(Pageable pageable) {
        return doctorRepository.findAll(pageable).map(DoctorMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public DoctorDTO findById(Long id) {
        return doctorRepository.findById(id)
                .map(DoctorMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));
    }

    @Transactional
    public DoctorDTO save(DoctorRequestDTO req) {
        Role roleDoctor = roleRepository.findByName("ROLE_DOCTOR")
                .orElseThrow(() -> new RuntimeException("Rol ROLE_DOCTOR no existe en BD"));

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setFullName(req.getName() + " " + req.getLastName());
        user.setRole(roleDoctor);

        user.setPassword(req.getPassword());

        userRepository.save(user);

        Doctor doctor = new Doctor();
        doctor.setName(req.getName());
        doctor.setLastName(req.getLastName());
        doctor.setSpecialty(req.getSpecialty());
        doctor.setUser(user); // Vinculación
        doctor.setIsActive(true);

        return DoctorMapper.toDTO(doctorRepository.save(doctor));
    }

    @Transactional
    public DoctorDTO update(Long id, DoctorRequestDTO req) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        doctor.setName(req.getName());
        doctor.setLastName(req.getLastName());
        doctor.setSpecialty(req.getSpecialty());

        return DoctorMapper.toDTO(doctorRepository.save(doctor));
    }

    @Transactional
    public void delete(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        doctor.setIsActive(false);
        doctorRepository.save(doctor);

        if(doctor.getUser() != null) {
            doctor.getUser().setIsActive(false);
            userRepository.save(doctor.getUser());
        }
    }
}