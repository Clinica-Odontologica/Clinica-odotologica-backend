package com.joao.dev.clinica_odontologica.service;

import com.joao.dev.clinica_odontologica.dto.usuario.UserRequestDTO;
import com.joao.dev.clinica_odontologica.dto.usuario.UserResponseDTO;
import com.joao.dev.clinica_odontologica.dto.usuario.UserUpdateRequestDTO;
import com.joao.dev.clinica_odontologica.entity.Doctor;
import com.joao.dev.clinica_odontologica.entity.User;
import com.joao.dev.clinica_odontologica.exceptions.EntityAlreadyExistsException;
import com.joao.dev.clinica_odontologica.mapper.UsuarioMapper;
import com.joao.dev.clinica_odontologica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.joao.dev.clinica_odontologica.mapper.UsuarioMapper.toEntity;

@RequiredArgsConstructor
@Service
public class UserService {
    private  final UserRepository userRepository;

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
        if (userRepository.existsByUsername(usuarioRequestDTO.getUsername())){
            throw new EntityAlreadyExistsException("El username " + usuarioRequestDTO.getUsername() + " ya está en uso.");
        }

        User user = toEntity(usuarioRequestDTO);
        User savedUser = userRepository.save(user);
        return UsuarioMapper.toDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserUpdateRequestDTO userUpdateRequestDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario not found with id: " + id));

        existingUser.setUsername(userUpdateRequestDTO.getUsername());
        existingUser.setPassword(userUpdateRequestDTO.getPassword());
        existingUser.setFullName(userUpdateRequestDTO.getFullname());
        existingUser.setEmail(userUpdateRequestDTO.getEmail());
        existingUser.setRole(userUpdateRequestDTO.getRol());

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