package com.joao.dev.clinica_odontologica.repository;

import com.joao.dev.clinica_odontologica.entity.TurnService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnServiceRepository extends JpaRepository<TurnService, Long> {
    List<TurnService> findByTurnId(Long turnId);
}
