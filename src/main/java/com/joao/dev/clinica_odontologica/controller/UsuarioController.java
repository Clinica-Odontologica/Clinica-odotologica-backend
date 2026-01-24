package com.joao.dev.clinica_odontologica.controller;

import com.joao.dev.clinica_odontologica.config.Constant;
import com.joao.dev.clinica_odontologica.dto.GlobalResponse;
import com.joao.dev.clinica_odontologica.dto.usuario.UserRequestDTO;
import com.joao.dev.clinica_odontologica.dto.usuario.UserResponseDTO;
import com.joao.dev.clinica_odontologica.dto.usuario.UserUpdateRequestDTO;
import com.joao.dev.clinica_odontologica.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constant.API_VERSION + "/" + Constant.TABLE_USUARIOS)
@Tag(name = "Usuarios (Admin)", description = "Gestión administrativa de cuentas de usuario (CRUD completo)")
public class UsuarioController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    public ResponseEntity<GlobalResponse<UserResponseDTO>> findById(@PathVariable Long id) {
        UserResponseDTO data = userService.getUsuarioById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Usuario retrieved successfully - id: " + id));
    }

    @PostMapping("/save")
    @Operation(
            summary = "Crear nuevo usuario (Administrativo)",
            description = "Endpoint para que un Admin cree manualmente otros usuarios (ej: crear una cuenta para una nueva recepcionista). Se diferencia del registro público en que aquí se puede asignar el rol directamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o username/email ya existen", content = @Content)
    })
    public ResponseEntity<GlobalResponse<UserResponseDTO>> saveUsuario(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        System.out.println("usuarioRequestDTO = " + userRequestDTO);
        UserResponseDTO data = userService.save(userRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success(data, "Usuario created successfully - id: " + data.getId()));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar datos de usuario", description = "Permite modificar nombre, rol o estado del usuario.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    })
    public ResponseEntity<GlobalResponse<UserResponseDTO>> updateUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO userUpdateRequestDTO
    ) {
        UserResponseDTO data = userService.update(id, userUpdateRequestDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Usuario updated successfully - id: " + id));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Eliminar / Desactivar Usuario",
            description = "Realiza un borrado (físico o lógico según implementación del servicio) de la cuenta de usuario."
    )
    public ResponseEntity<GlobalResponse<Object>> deleteUsuario(@PathVariable Long id) {
        userService.deleteUsuario(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(null, "Usuario deleted successfully - id: " + id));
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios paginados", description = "Ideal para la tabla de gestión de usuarios en el panel de administración.")
    public ResponseEntity<GlobalResponse<Page<UserResponseDTO>>> getAllUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDTO> data = userService.getAllUsuarios(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Usuarios retrieved successfully"));
    }

}