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
        configuration.addAllowedOrigin("http://localhost:5173");/*Puerto de VITE*/
        configuration.addAllowedOrigin("http://localhost:81");/*Puerto de nginx*/
        configuration.addAllowedOrigin("http://localhost:8081");/*Puerto de SWAGGER*/
        configuration.addAllowedOriginPattern("https://*.trycloudflare.com");/*Puerto para tunnels de cloudflare*/
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
                        // 0. SOLUCIÓN CORS PREFLIGHT (¡VITAL PARA FRONTEND!)
                        // ====================================================
                        .requestMatchers(HttpMethod.OPTIONS, "/**")
                        .permitAll()

                        // ====================================================
                        // 1. ENDPOINTS PÚBLICOS (Sin Token)
                        // ====================================================
                        .requestMatchers("/api/v1/auth/**")
                        .permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .permitAll()

                        // ====================================================
                        // 2. GESTIÓN DE USUARIOS (Solo Admin)
                        // ====================================================
                        // Permitimos que CUALQUIER usuario logueado pueda ver y actualizar su propio perfil
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/update/{id}").authenticated()
                        // Pero solo el ADMIN puede crear, listar todos, o borrar usuarios
                        .requestMatchers("/api/v1/usuarios/**").hasAuthority("ROLE_ADMIN")


                        // ====================================================
                        // 3. GESTIÓN DE DOCTORES
                        // ====================================================
                        // Permitimos que el DOCTOR consulte su propio perfil
                        .requestMatchers(HttpMethod.GET, "/api/v1/doctores/user/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_DOCTOR")

                        .requestMatchers(HttpMethod.GET, "/api/v1/doctores/active-list")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_RECEPTIONIST")

                        // El resto de la paginación y buscar por ID sigue siendo solo para Admin
                        .requestMatchers(HttpMethod.GET, "/api/v1/doctores/{id}", "/api/v1/doctores/dashboard-paginated/**")
                        .hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/api/v1/doctores/**")
                        .hasAuthority("ROLE_ADMIN")

                        // ====================================================
                        // 4. GESTIÓN DE PACIENTES
                        // ====================================================
                        .requestMatchers(HttpMethod.GET, "/api/v1/pacientes/**")
                        .hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN", "ROLE_DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/pacientes/**")
                        .hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/pacientes/**")
                        .hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/pacientes/**")
                        .hasAuthority("ROLE_ADMIN")

                        // ====================================================
                        // 5. GESTIÓN DE SERVICIOS/TRATAMIENTOS
                        // ====================================================
                        .requestMatchers(HttpMethod.GET, "/api/v1/tratamientos/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/tratamientos/**")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/tratamientos/**")
                        .hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/tratamientos/**")
                        .hasAuthority("ROLE_ADMIN")

                        // ====================================================
                        // 6. GESTIÓN DE TURNOS (Agenda)
                        // ====================================================
                        // El doctor necesita ver sus turnos
                        .requestMatchers(HttpMethod.GET, "/api/v1/turns/**", "/api/v1/turnos/**")
                        .hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN", "ROLE_DOCTOR")
                        .requestMatchers(HttpMethod.POST, "/api/v1/turns/**", "/api/v1/turnos/**")
                        .hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/turns/**", "/api/v1/turnos/**")
                        .hasAnyAuthority("ROLE_RECEPTIONIST", "ROLE_ADMIN")

                        // ====================================================
                        // 7. HISTORIA CLÍNICA (Área Médica)
                        // ====================================================
                        // 🌟 CORRECCIÓN PREVENTIVA: Añadido "/api/v1/clinica/**" que es el que usa tu frontend
                        .requestMatchers(HttpMethod.POST, "/api/v1/clinical-entries/**", "/api/v1/historias/**", "/api/v1/clinica/**")
                        .hasAnyAuthority("ROLE_DOCTOR", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/clinical-entries/**", "/api/v1/historias/**", "/api/v1/clinica/**")
                        .hasAnyAuthority("ROLE_DOCTOR", "ROLE_ADMIN")

                        // -------------------------------------------------
                        // Todo lo demás requiere autenticación
                        // -------------------------------------------------
                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        return http.build();
    }
}