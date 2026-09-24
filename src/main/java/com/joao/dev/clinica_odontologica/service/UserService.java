package com.joao.dev.clinica_odontologica.service;

import com.joao.dev.clinica_odontologica.dto.usuario.UserRequestDTO;
import com.joao.dev.clinica_odontologica.dto.usuario.UserResponseDTO;
import com.joao.dev.clinica_odontologica.dto.usuario.UserUpdateRequestDTO;
import com.joao.dev.clinica_odontologica.entity.Role;
import com.joao.dev.clinica_odontologica.entity.User;
import com.joao.dev.clinica_odontologica.exceptions.EntityAlreadyExistsException;
import com.joao.dev.clinica_odontologica.mapper.UsuarioMapper;
import com.joao.dev.clinica_odontologica.repository.RoleRepository;
import com.joao.dev.clinica_odontologica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.joao.dev.clinica_odontologica.mapper.UsuarioMapper.toEntity;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // <-- 1. Inyectar RoleRepository
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsuarios(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UsuarioMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUsuarioById(Long id) {
        return userRepository.findById(id)
                .map(UsuarioMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Usuario not found with id: " + id));
    }

    @Transactional
    public UserResponseDTO save(UserRequestDTO usuarioRequestDTO) {
        if (userRepository.existsByEmail(usuarioRequestDTO.getEmail())) {
            throw new EntityAlreadyExistsException("El email " + usuarioRequestDTO.getEmail() + " ya está en uso.");
        }
        if (userRepository.existsByUsername(usuarioRequestDTO.getUsername())) {
            throw new EntityAlreadyExistsException("El username " + usuarioRequestDTO.getUsername() + " ya está en uso.");
        }

        User user = toEntity(usuarioRequestDTO);
        user.setPassword(passwordEncoder.encode(usuarioRequestDTO.getPassword()));

        User savedUser = userRepository.save(user);
        return UsuarioMapper.toDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario not found with id: " + id));

        existingUser.setUsername(userUpdateRequestDTO.getUsername());

        if (userUpdateRequestDTO.getPassword() != null && !userUpdateRequestDTO.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateRequestDTO.getPassword()));
        }

        existingUser.setFullName(userUpdateRequestDTO.getFullname());
        existingUser.setEmail(userUpdateRequestDTO.getEmail());

        if (userUpdateRequestDTO.getRol() != null) {
            String roleName = userUpdateRequestDTO.getRol().getName();

            Role managedRole = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado en la base de datos: " + roleName));

            existingUser.setRole(managedRole);
        }

        existingUser.setIsActive(userUpdateRequestDTO.getIsActive());

        User updatedUser = userRepository.save(existingUser);
        return UsuarioMapper.toDTO(updatedUser);
    }

    @Transactional
    public void deleteUsuario(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setIsActive(false);
        userRepository.save(user);
    }
}