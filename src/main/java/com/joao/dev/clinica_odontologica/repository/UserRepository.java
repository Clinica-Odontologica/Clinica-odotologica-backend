package com.joao.dev.clinica_odontologica.repository;

import com.joao.dev.clinica_odontologica.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    // Para validar que no se repitan al registrar
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
