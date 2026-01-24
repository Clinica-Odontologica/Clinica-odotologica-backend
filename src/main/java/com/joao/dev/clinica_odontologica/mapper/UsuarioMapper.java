package com.joao.dev.clinica_odontologica.mapper;

import com.joao.dev.clinica_odontologica.dto.usuario.UserRequestDTO;
import com.joao.dev.clinica_odontologica.dto.usuario.UserResponseDTO;
import com.joao.dev.clinica_odontologica.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {
    public static UserResponseDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setFullname(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRol(user.getRole());
        dto.setActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public static User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user;
    }

}
