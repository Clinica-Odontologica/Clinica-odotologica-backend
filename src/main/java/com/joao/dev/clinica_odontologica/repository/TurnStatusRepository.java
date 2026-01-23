package com.joao.dev.clinica_odontologica.repository;

import com.joao.dev.clinica_odontologica.entity.TurnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TurnStatusRepository extends JpaRepository<TurnStatus, Integer> {
    Optional<TurnStatus> findByName(String name);
}
