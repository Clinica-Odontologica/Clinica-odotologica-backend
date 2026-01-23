package com.joao.dev.clinica_odontologica.repository;

import com.joao.dev.clinica_odontologica.entity.Turn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TurnRepository extends JpaRepository<Turn, Long> {

    // 1. Historial de un paciente
    List<Turn> findByPatientIdOrderByAppointmentDateTimeDesc(Long patientId);

    // 2. Agenda de un doctor en un rango de fechas (Ej: Turnos de HOY)
    List<Turn> findByDoctorIdAndAppointmentDateTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    // 3. Sala de Espera: Turnos por estado (Ej: Todos los "EN_ESPERA")
    List<Turn> findByStatusName(String statusName);

    // 4. Validar disponibilidad: ¿Ya tiene turno ese doctor a esa hora?
    boolean existsByDoctorIdAndAppointmentDateTime(Long doctorId, LocalDateTime appointmentDateTime);
}
