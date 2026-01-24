package com.joao.dev.clinica_odontologica.controller;

import com.joao.dev.clinica_odontologica.config.Constant;
import com.joao.dev.clinica_odontologica.dto.GlobalResponse;
import com.joao.dev.clinica_odontologica.dto.service.ServiceDTO;
import com.joao.dev.clinica_odontologica.service.TreatmentService;
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

import java.util.List;

@RestController
@RequestMapping(Constant.API_VERSION + "/" + Constant.TABLE_TREATMENT)
@RequiredArgsConstructor
@Tag(name = "Tratamientos (Servicios)", description = "Gestión del catálogo de precios y servicios ofrecidos por la clínica")
public class TreatmentController {

    private final TreatmentService treatmentService;

    @GetMapping("/active-list")
    @Operation(
            summary = "Listar servicios activos (Dropdown)",
            description = "Devuelve todos los tratamientos disponibles para realizar. Se usa para llenar el selector de servicios al crear un turno."
    )
    @ApiResponse(responseCode = "200", description = "Lista recuperada",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceDTO.class)))
    public ResponseEntity<GlobalResponse<List<ServiceDTO>>> getActiveServices() {
        List<ServiceDTO> data = treatmentService.findAllActive();
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Lista de servicios activos"));
    }

    @PostMapping("/save")
    @Operation(
            summary = "Crear nuevo servicio",
            description = "Agrega un nuevo tratamiento al menú de precios de la clínica."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Servicio creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    public ResponseEntity<GlobalResponse<ServiceDTO>> createService(@Valid @RequestBody ServiceDTO dto) {
        ServiceDTO data = treatmentService.createService(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success(data, "Servicio creado correctamente"));
    }

    @GetMapping("/dashboard-paginated")
    @Operation(summary = "Catálogo completo paginado (Admin)")
    public ResponseEntity<GlobalResponse<Page<ServiceDTO>>> getAllServices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ServiceDTO> data = treatmentService.findAllPaginated(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Lista de servicios paginada"));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Desactivar servicio",
            description = "Realiza un borrado lógico del servicio. Ya no aparecerá en el selector de turnos nuevos, pero se mantiene en el historial de turnos pasados."
    )
    public ResponseEntity<GlobalResponse<ServiceDTO>> deleteService(@PathVariable Long id) {
        treatmentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(null, "Servicio desactivado correctamente - ID: " + id));
    }

}
