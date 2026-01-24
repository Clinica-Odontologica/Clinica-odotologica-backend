package com.joao.dev.clinica_odontologica.mapper;

import com.joao.dev.clinica_odontologica.dto.service.ServiceDTO;
import com.joao.dev.clinica_odontologica.entity.Service;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public static ServiceDTO toDTO(Service service) {
        if (service == null) return null;

        ServiceDTO dto = new ServiceDTO();
        dto.setId(service.getId());
        dto.setName(service.getName());
        dto.setBasePrice(service.getBasePrice());
        return dto;
    }

    public static Service toEntity(ServiceDTO dto) {
        if (dto == null) return null;

        Service service = new Service();
        service.setName(dto.getName());
        service.setBasePrice(dto.getBasePrice());
        return service;
    }
}