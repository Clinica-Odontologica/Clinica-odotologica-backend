package com.joao.dev.clinica_odontologica.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173");
        configuration.addAllowedOrigin("http://localhost:8081");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                                // ====================================================
                                // 1. ENDPOINTS PÚBLICOS (Sin Token)
                                // ====================================================
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll() // Login y Register
                                // Swagger UI y Documentación API
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                                // ====================================================
                                // 2. GESTIÓN DE USUARIOS Y DOCTORES (Solo Admin)
                                // ====================================================
                                // Solo el admin puede crear/borrar usuarios y doctores
                                .requestMatchers("/api/v1/usuarios/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers("/api/v1/doctors/save", "/api/v1/doctors/delete/**").hasAuthority("ROLE_ADMIN")

                                // Los selectores de doctores los pueden ver todos los logueados
                                .requestMatchers("/api/v1/doctors/active-list", "/api/v1/doctors/{id}").authenticated()

                                // ====================================================
                                // 3. GESTIÓN DE PACIENTES (Recepción y Admin)
                                // ====================================================
                                // Crear, Editar, Buscar DNI: Recepción y Admin
                                .requestMatchers(HttpMethod.POST, "/api/v1/patients/**").hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/patients/**").hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/patients/**").hasAuthority("ROLE_ADMIN") // Solo admin borra
                                // Ver pacientes: Tambien los doctores necesitan ver datos basicos
                                .requestMatchers(HttpMethod.GET, "/api/v1/patients/**").hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN", "ROLE_DOCTOR")

                                // ====================================================
                                // 4. GESTIÓN DE SERVICIOS/TRATAMIENTOS
                                // ====================================================
                                // Admin configura precios
                                .requestMatchers(HttpMethod.POST, "/api/v1/services/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/services/**").hasAuthority("ROLE_ADMIN")
                                // Todos necesitan ver la lista para agendar
                                .requestMatchers(HttpMethod.GET, "/api/v1/services/**").authenticated()

                                // ====================================================
                                // 5. GESTIÓN DE TURNOS (Agenda)
                                // ====================================================
                                // Crear turnos: Recepción (Principalmente) y Admin
                                .requestMatchers(HttpMethod.POST, "/api/v1/turns/**").hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN")
                                // Cancelar turnos
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/turns/**").hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN")
                                // Ver Agenda: Todos (El doctor necesita ver qué pacientes tiene)
                                .requestMatchers(HttpMethod.GET, "/api/v1/turns/**").authenticated()

                                // ====================================================
                                // 6. HISTORIA CLÍNICA (Área Médica)
                                // ====================================================
                                // Escribir diagnóstico: SOLO DOCTORES
                                .requestMatchers(HttpMethod.POST, "/api/v1/clinical-entries/**").hasAuthority("ROLE_DOCTOR")
                                // Leer historial: Doctores y Admin (Recepción no debería ver detalles médicos sensibles)
                                .requestMatchers(HttpMethod.GET, "/api/v1/clinical-entries/**").hasAnyAuthority("ROLE_DOCTOR", "ROLE_ADMIN")
                                // -------------------------------------------------
                                // Todo lo demás requiere autenticación
                                // -------------------------------------------------
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }
}

