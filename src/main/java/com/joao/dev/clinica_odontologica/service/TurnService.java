package com.joao.dev.clinica_odontologica.service;

import com.joao.dev.clinica_odontologica.dto.turn.TurnRequestDTO;
import com.joao.dev.clinica_odontologica.dto.turn.TurnResponseDTO;
import com.joao.dev.clinica_odontologica.entity.*;
import com.joao.dev.clinica_odontologica.mapper.TurnMapper;
import com.joao.dev.clinica_odontologica.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnService {

    private final TurnRepository turnRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ServiceRepository serviceRepository;
    private final TurnStatusRepository turnStatusRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Page<TurnResponseDTO> findAll(Pageable pageable) {
        return turnRepository.findAll(pageable)
                .map(TurnMapper::toDTO);
    }
    @Transactional(readOnly = true)
    public TurnResponseDTO findById(Long id) {
        Turn turn = turnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Turno no encontrado con id: " + id));
        return TurnMapper.toDTO(turn);
    }

    @Transactional
    public TurnResponseDTO save(TurnRequestDTO request, Long createdByUserId) {
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        User createdByUser = userRepository.findById(createdByUserId)
                .orElseThrow(() -> new RuntimeException("Usuario recepcionista no encontrado"));

        TurnStatus statusPendiente = turnStatusRepository.findByName("PENDIENTE") // O "ESPERA" segun tu BD
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no configurado en BD"));

        Turn turn = new Turn();
        turn.setPatient(patient);
        turn.setDoctor(doctor);
        turn.setCreatedByUser(createdByUser);
        turn.setStatus(statusPendiente);

        turn.setAppointmentDateTime(
                request.getAppointmentDate() != null ? request.getAppointmentDate() : LocalDateTime.now()
        );

        List<com.joao.dev.clinica_odontologica.entity.TurnService> turnServices = new ArrayList<>();

        for (Long serviceId : request.getServiceIds()) {
            com.joao.dev.clinica_odontologica.entity.Service dbService = serviceRepository.findById(serviceId)
                    .orElseThrow(() -> new RuntimeException("Servicio ID " + serviceId + " no existe"));

            com.joao.dev.clinica_odontologica.entity.TurnService ts = new com.joao.dev.clinica_odontologica.entity.TurnService();
            ts.setTurn(turn);
            ts.setService(dbService);

            ts.setPriceAtTime(dbService.getBasePrice());

            turnServices.add(ts);
        }

        turn.setServices(turnServices);

        Turn savedTurn = turnRepository.save(turn);

        return TurnMapper.toDTO(savedTurn);
    }
    @Transactional
    public void delete(Long id) {
        Turn turn = turnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        turn.setIsActive(false);
        turnRepository.save(turn);

    }
}