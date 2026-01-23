package com.joao.dev.clinica_odontologica.repository;

import com.joao.dev.clinica_odontologica.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findByIsActiveTrue();

    Optional<Doctor> findByUserId(Long userId);
}
