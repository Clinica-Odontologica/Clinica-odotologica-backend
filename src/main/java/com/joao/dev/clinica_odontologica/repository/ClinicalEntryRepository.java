package com.joao.dev.clinica_odontologica.repository;

import com.joao.dev.clinica_odontologica.entity.ClinicalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClinicalEntryRepository extends JpaRepository<ClinicalEntry, Long> {

    Optional<ClinicalEntry> findByTurnId(Long turnId);
}