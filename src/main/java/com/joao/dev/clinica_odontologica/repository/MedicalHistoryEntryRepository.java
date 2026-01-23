package com.joao.dev.clinica_odontologica.repository;

import com.joao.dev.clinica_odontologica.entity.MedicalHistoryEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalHistoryEntryRepository extends JpaRepository<MedicalHistoryEntry, Long> {
    List<MedicalHistoryEntry> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}