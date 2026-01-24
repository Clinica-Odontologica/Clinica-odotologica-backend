package com.joao.dev.clinica_odontologica.controller;

import com.joao.dev.clinica_odontologica.config.Constant;
import com.joao.dev.clinica_odontologica.dto.GlobalResponse;
import com.joao.dev.clinica_odontologica.dto.clinical.ClinicalEntryRequestDTO;
import com.joao.dev.clinica_odontologica.dto.clinical.ClinicalEntryResponseDTO;
import com.joao.dev.clinica_odontologica.service.ClinicalEntryService;
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
@RequestMapping(Constant.API_VERSION + "/" + Constant.TABLE_CLINICAL)
@RequiredArgsConstructor
@Tag(name = "Historias Clínicas", description = "Gestión de diagnósticos, evolución del paciente y cierre de turnos")
public class ClinicalEntryController {

    private final ClinicalEntryService clinicalEntryService;

    @GetMapping("/dashboard-paginated")
    @Operation(
            summary = "Listar historias clínicas paginadas",
            description = "Recupera el listado de evoluciones/diagnósticos registrados en el sistema de forma paginada. Útil para el historial general."
    )
    @ApiResponse(responseCode = "200", description = "Listado recuperado exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ClinicalEntryResponseDTO.class)))
    public ResponseEntity<GlobalResponse<Page<ClinicalEntryResponseDTO>>> getAllClinical(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ClinicalEntryResponseDTO> data = clinicalEntryService.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Historias clínicas paginadas recuperadas"));
    }
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener detalle de una historia clínica",
            description = "Busca una nota clínica específica por su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Historia encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClinicalEntryResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Historia clínica no encontrada", content = @Content)
    })
    public ResponseEntity<GlobalResponse<ClinicalEntryResponseDTO>> getClinicalById(@PathVariable Long id) {
        ClinicalEntryResponseDTO data = clinicalEntryService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Detalle de la historia clínica recuperada"));
    }

    @PostMapping("/save")
    @Operation(
            summary = "Registrar diagnóstico y cerrar turno",
            description = "Guarda la evolución del paciente, diagnósticos y notas. **IMPORTANTE:** Esta acción cambia automáticamente el estado del turno asociado a 'COMPLETADO' (DONE)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Historia registrada y turno cerrado correctamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o el turno ya estaba cerrado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Turno asociado no encontrado", content = @Content)
    })
    public ResponseEntity<GlobalResponse<Void>> createEntry(@Valid @RequestBody ClinicalEntryRequestDTO request) {
        clinicalEntryService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success(null, "Historia clínica registrada y Turno cerrado."));
    }
}
