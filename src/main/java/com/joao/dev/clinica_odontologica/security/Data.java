package com.joao.dev.clinica_odontologica.security;

import com.joao.dev.clinica_odontologica.entity.User;
import com.joao.dev.clinica_odontologica.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Data implements ApplicationRunner {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(ApplicationArguments args) {

        List<User> usuarios = userRepository.findAll();

        if (usuarios.isEmpty()) {
            System.out.println("⚠️ No hay usuarios registrados en la base de datos.");
            return;
        }

        // Encriptar y guardar cada usuario
        usuarios.forEach(user -> {
            String passwordActual = user.getPassword();

            // Evitar encriptar dos veces si ya está encriptado
            if (!passwordActual.startsWith("$2a$")) {
                String passwordEncriptado = bCryptPasswordEncoder.encode(passwordActual);
                user.setPassword(passwordEncriptado);
            }
        });

        userRepository.saveAll(usuarios);

        System.out.println("✅ Proceso de encriptación de contraseñas completado.");
    }
}
