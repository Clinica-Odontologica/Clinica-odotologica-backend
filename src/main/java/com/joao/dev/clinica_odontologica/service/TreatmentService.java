package com.joao.dev.clinica_odontologica.service;

import com.joao.dev.clinica_odontologica.dto.service.ServiceDTO;
import com.joao.dev.clinica_odontologica.entity.User;
import com.joao.dev.clinica_odontologica.mapper.ServiceMapper;
import com.joao.dev.clinica_odontologica.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service; // Spring Service
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TreatmentService {

    private final ServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    public List<ServiceDTO> findAllActive() {
        return serviceRepository.findByIsActiveTrue().stream()
                .map(ServiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ServiceDTO createService(ServiceDTO dto) {
        com.joao.dev.clinica_odontologica.entity.Service entity = ServiceMapper.toEntity(dto);
        entity.setIsActive(true);
        return ServiceMapper.toDTO(serviceRepository.save(entity));
    }

    @Transactional
    public Page<ServiceDTO> findAllPaginated(Pageable pageable) {
        return serviceRepository.findAll(pageable)
                .map(ServiceMapper::toDTO);
    }

    @Transactional
    public ServiceDTO findById(Long id) {
        com.joao.dev.clinica_odontologica.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        return ServiceMapper.toDTO(service);
    }

    @Transactional
    public void delete(Long id) {
        com.joao.dev.clinica_odontologica.entity.Service service  = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        service.setIsActive(false);
        serviceRepository.save(service);

    }

}