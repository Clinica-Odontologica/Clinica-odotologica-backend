package com.joao.dev.clinica_odontologica.mapper;

import com.joao.dev.clinica_odontologica.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public static String toDTO(Role role) {
        if (role == null) return null;
        return role.getName();
    }
}