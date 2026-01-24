package com.joao.dev.clinica_odontologica.mapper;

import com.joao.dev.clinica_odontologica.dto.service.ServiceDTO;
import com.joao.dev.clinica_odontologica.dto.turn.TurnResponseDTO;
import com.joao.dev.clinica_odontologica.entity.Turn;
import com.joao.dev.clinica_odontologica.entity.TurnService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TurnMapper {

    public static TurnResponseDTO toDTO(Turn turn) {
        if (turn == null) return null;

        TurnResponseDTO dto = new TurnResponseDTO();
        dto.setId(turn.getId());

        if (turn.getPatient() != null) {
            dto.setPatientId(turn.getPatient().getId());
            dto.setPatientName(turn.getPatient().getName() + " " + turn.getPatient().getLast_name());
            dto.setPatientDni(turn.getPatient().getDni());
        }

        if (turn.getDoctor() != null) {
            dto.setDoctorId(turn.getDoctor().getId());
            dto.setDoctorName(turn.getDoctor().getName() + " " + turn.getDoctor().getLastName());
            dto.setDoctorSpecialty(turn.getDoctor().getSpecialty());
        }

        if (turn.getStatus() != null) {
            dto.setStatus(turn.getStatus().getName());
        }
        dto.setAppointmentDate(turn.getAppointmentDateTime());

        List<ServiceDTO> serviceDTOS = new ArrayList<>();
        BigDecimal totalCost = BigDecimal.ZERO;

        if (turn.getServices() != null) {
            serviceDTOS = turn.getServices().stream()
                    .map(ts -> {
                        ServiceDTO sDto = new ServiceDTO();
                        sDto.setId(ts.getService().getId()); // ID del servicio original
                        sDto.setName(ts.getService().getName());
                        sDto.setBasePrice(ts.getPriceAtTime()); // Usamos el precio histórico guardado
                        return sDto;
                    })
                    .collect(Collectors.toList());

            // 2. Sumar el total
            totalCost = turn.getServices().stream()
                    .map(TurnService::getPriceAtTime)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        dto.setServices(serviceDTOS);
        dto.setTotalCost(totalCost);

        return dto;
    }

}