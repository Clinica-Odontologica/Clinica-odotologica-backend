package com.joao.dev.clinica_odontologica.controller;

import com.joao.dev.clinica_odontologica.config.Constant;
import com.joao.dev.clinica_odontologica.dto.GlobalResponse;
import com.joao.dev.clinica_odontologica.dto.patient.PatientDTO;
import com.joao.dev.clinica_odontologica.service.PatientService;
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
@RequestMapping(Constant.API_VERSION + "/" + Constant.TABLE_PATIENTS)
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gestión de datos personales, historial y búsquedas rápidas")
public class PatientController {
    private final PatientService patientService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar paciente por ID interno")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Paciente no encontrado", content = @Content)
    })
    public ResponseEntity<GlobalResponse<PatientDTO>> findById(@PathVariable Long id) {
        PatientDTO data = patientService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Patient retrieved successfully - id: " + id));
    }

    @GetMapping("/dni/{dni}")
    @Operation(
            summary = "Buscar paciente por DNI",
            description = "Endpoint crítico para Recepción. Permite verificar rápidamente si un paciente ya existe antes de crear un turno."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe paciente con ese DNI", content = @Content)
    })
    public ResponseEntity<GlobalResponse<PatientDTO>> findByDni(@PathVariable String dni) {
        PatientDTO data = patientService.findByDni(dni);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Patient found by DNI: " + dni));
    }
    @Operation(
            summary = "Registrar o Actualizar Paciente",
            description = "Crea un nuevo expediente de paciente. Si el paciente ya existe (DNI duplicado), devolverá error."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o DNI ya registrado", content = @Content)
    })
    @PostMapping("/save")
    public ResponseEntity<GlobalResponse<PatientDTO>> savePatient(@Valid @RequestBody PatientDTO patientDTO) {
        PatientDTO data = patientService.save(patientDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success(data, "Patient created successfully - id: " + data.getId()));
    }

    @GetMapping("/dashboard-paginated")
    @Operation(summary = "Listado paginado de pacientes (Admin Dashboard)")
    public ResponseEntity<GlobalResponse<Page<PatientDTO>>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PatientDTO> data = patientService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Patients retrieved successfully"));
    }
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar Paciente (Borrado Lógico)",
            description = "Desactiva al paciente del sistema (`isActive = false`) para mantener la integridad histórica de sus turnos."
    )
    public ResponseEntity<GlobalResponse<Void>> deletePatient(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(null, "Patient deactivated successfully - id: " + id));
    }
}
