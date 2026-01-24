package com.joao.dev.clinica_odontologica.controller;

import com.joao.dev.clinica_odontologica.config.Constant;
import com.joao.dev.clinica_odontologica.dto.GlobalResponse;
import com.joao.dev.clinica_odontologica.dto.turn.TurnRequestDTO;
import com.joao.dev.clinica_odontologica.dto.turn.TurnResponseDTO;
import com.joao.dev.clinica_odontologica.service.TurnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping(Constant.API_VERSION + "/" + Constant.TABLE_TURN)
@RequiredArgsConstructor
@Tag(name = "Turnos (Agenda)", description = "Gestión central de citas médicas. Creación, listado y cancelación.")
public class TurnController {

    private final TurnService turnService;

    @PostMapping("/save")
    @Operation(
            summary = "Reservar un nuevo turno",
            description = "Registra una cita vinculando Paciente + Doctor + Servicios. " +
                    "**Calcula automáticamente el costo total** basándose en los precios actuales de los servicios seleccionados y establece el estado inicial como 'PENDIENTE'."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Turno agendado exitosamente",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TurnResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej: Doctor no disponible)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Paciente o Doctor no encontrados", content = @Content)
    })
    public ResponseEntity<GlobalResponse<TurnResponseDTO>> createTurn(
            @Valid @RequestBody TurnRequestDTO request,
            @Parameter(description = "ID del usuario (Recepcionista) que está creando el turno", example = "1")
            @RequestParam Long userId
    ) {
        TurnResponseDTO data = turnService.save(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success(data, "Turno generado correctamente - ID: " + data.getId()));
    }

    @GetMapping("/dashboard-paginated")
    @Operation(
            summary = "Ver agenda completa (Dashboard)",
            description = "Muestra todos los turnos del sistema de forma paginada. Ideal para la vista principal de la recepción."
    )
    public ResponseEntity<GlobalResponse<Page<TurnResponseDTO>>> getAllTurns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TurnResponseDTO> data = turnService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Cola de turnos recuperada"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Ver detalle de un turno", description = "Muestra la información completa: Paciente, Doctor y la lista de servicios contratados con sus precios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Turno encontrado"),
            @ApiResponse(responseCode = "404", description = "Turno no existe", content = @Content)
    })
    public ResponseEntity<GlobalResponse<TurnResponseDTO>> getTurnById(@PathVariable Long id) {
        TurnResponseDTO data = turnService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Detalle del turno recuperado"));
    }

    @DeleteMapping
    @Operation(
            summary = "Cancelar Turno",
            description = "Cambia el estado del turno a 'CANCELADO' o lo desactiva lógicamente. **Nota:** Se usa un parámetro de consulta (?id=1) en lugar de ruta."
    )
    public ResponseEntity<GlobalResponse<TurnResponseDTO>> deleteTurn(@RequestParam Long id) {
        turnService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(null, "Turno desactivado correctamente - ID: " + id));
    }
}