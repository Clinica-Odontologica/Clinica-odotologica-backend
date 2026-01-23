package com.joao.dev.clinica_odontologica.service;

import com.joao.dev.clinica_odontologica.dto.clinical.ClinicalEntryRequestDTO;
import com.joao.dev.clinica_odontologica.dto.clinical.ClinicalEntryResponseDTO;
import com.joao.dev.clinica_odontologica.entity.ClinicalEntry;
import com.joao.dev.clinica_odontologica.entity.Turn;
import com.joao.dev.clinica_odontologica.entity.TurnStatus;
import com.joao.dev.clinica_odontologica.mapper.ClinicalEntryMapper;
import com.joao.dev.clinica_odontologica.repository.ClinicalEntryRepository;
import com.joao.dev.clinica_odontologica.repository.TurnRepository;
import com.joao.dev.clinica_odontologica.repository.TurnStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClinicalEntryService {

    private final ClinicalEntryRepository clinicalEntryRepository;
    private final TurnRepository turnRepository;
    private final TurnStatusRepository turnStatusRepository;

    @Transactional
    public void save(ClinicalEntryRequestDTO request) {
        Turn turn = turnRepository.findById(request.getTurnId())
                .orElseThrow(() -> new RuntimeException("Turno no encontrado"));

        if ("DONE".equals(turn.getStatus().getName())) {
            throw new RuntimeException("Este turno ya fue finalizado anteriormente");
        }

        ClinicalEntry entry = ClinicalEntryMapper.toEntity(request);
        entry.setTurn(turn);
        clinicalEntryRepository.save(entry);

        TurnStatus statusDone = turnStatusRepository.findByName("PENDIENTE")
                .orElseThrow(() -> new RuntimeException("Estado PENDIENTE no existe en BD"));

        turn.setStatus(statusDone);
        turnRepository.save(turn);
    }

    public ClinicalEntryResponseDTO findById(Long id) {
        ClinicalEntry entry = clinicalEntryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada - id: " + id));
        return ClinicalEntryMapper.toDTO(entry);
    }

    public Page<ClinicalEntryResponseDTO> findAll(Pageable pageable){
        return clinicalEntryRepository.findAll(pageable).map(ClinicalEntryMapper::toDTO);
    }
}