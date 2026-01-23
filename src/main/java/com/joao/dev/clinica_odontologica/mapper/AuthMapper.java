package com.joao.dev.clinica_odontologica.mapper;

import com.joao.dev.clinica_odontologica.dto.auth.LoginResponseDTO;
import com.joao.dev.clinica_odontologica.dto.auth.RegisterRequestDTO;
import com.joao.dev.clinica_odontologica.dto.auth.RegisterResponseDTO;
import com.joao.dev.clinica_odontologica.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public static User toEntity(RegisterRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setFullName(dto.getFullname());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPasswordd());
        user.setRole(dto.getRole());

        return user;
    }

    public static RegisterResponseDTO registerToDTO(User user){
        if (user  == null) {
            return null;
        }

        RegisterResponseDTO dto = new RegisterResponseDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRol(user.getRole());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());

        return dto;
    }

    public static LoginResponseDTO loginToDTO(User user) {
        if (user == null) {
            return null;
        }

        LoginResponseDTO dto = new LoginResponseDTO();
        dto.setId(user.getId());
        dto.setFullname(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRol(user.getRole());
        dto.setActive(user.getIsActive());
        return dto;
    }
}
