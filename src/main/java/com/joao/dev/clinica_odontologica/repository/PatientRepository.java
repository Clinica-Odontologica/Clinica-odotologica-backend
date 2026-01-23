package com.joao.dev.clinica_odontologica.repository;

import com.joao.dev.clinica_odontologica.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByDni(String dni);

    List<Patient> findByNameContainingIgnoreCaseOrDniContaining(String name, String dni);
}
