package com.joao.dev.clinica_odontologica.service;

import com.joao.dev.clinica_odontologica.dto.service.ServiceDTO;
import com.joao.dev.clinica_odontologica.dto.usuario.UserResponseDTO;
import com.joao.dev.clinica_odontologica.mapper.ServiceMapper;
import com.joao.dev.clinica_odontologica.mapper.UsuarioMapper;
import com.joao.dev.clinica_odontologica.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service; // Spring Service
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.joao.dev.clinica_odontologica.mapper.ServiceMapper.toDTO;

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

    @Transactional(readOnly = true)
    public Page<ServiceDTO> getAllService(Pageable pageable) {
        return serviceRepository.findAll(pageable)
                .map(ServiceMapper::toDTO);
    }

    @Transactional
    public ServiceDTO createService(ServiceDTO dto) {
        com.joao.dev.clinica_odontologica.entity.Service entity = ServiceMapper.toEntity(dto);
        entity.setIsActive(true);
        return toDTO(serviceRepository.save(entity));
    }

    @Transactional
    public ServiceDTO findById(Long id) {
        com.joao.dev.clinica_odontologica.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        return toDTO(service);
    }

    @Transactional
    public ServiceDTO updateService(Long id, ServiceDTO dto) {
        com.joao.dev.clinica_odontologica.entity.Service existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));

        existingService.setName(dto.getName());
        existingService.setBasePrice(dto.getBasePrice());
        existingService.setIsActive(dto.getIsActive());

        com.joao.dev.clinica_odontologica.entity.Service updateService = serviceRepository.save(existingService);
        return ServiceMapper.toDTO(updateService);
    }

    @Transactional
    public void delete(Long id) {
        com.joao.dev.clinica_odontologica.entity.Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        service.setIsActive(false);
        serviceRepository.save(service);

    }

}