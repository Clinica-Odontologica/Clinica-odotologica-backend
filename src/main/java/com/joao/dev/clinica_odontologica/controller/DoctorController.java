package com.joao.dev.clinica_odontologica.controller;

import com.joao.dev.clinica_odontologica.config.Constant;
import com.joao.dev.clinica_odontologica.dto.GlobalResponse;
import com.joao.dev.clinica_odontologica.dto.doctor.DoctorDTO;
import com.joao.dev.clinica_odontologica.dto.doctor.DoctorRequestDTO;
import com.joao.dev.clinica_odontologica.service.DoctorService;
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
@RequestMapping(Constant.API_VERSION + "/" + Constant.TABLE_DOCTOR)
@RequiredArgsConstructor
@Tag(name = "Doctores", description = "Gestión del personal médico, altas, bajas y actualizaciones")
public class DoctorController {
    private final DoctorService doctorService;


    @GetMapping("/active-list")
    @Operation(
            summary = "Listar doctores activos (Para Selectores)",
            description = "Devuelve una lista simple de doctores que están activos. Ideal para llenar listas desplegables en el registro de turnos."
    )
    @ApiResponse(responseCode = "200", description = "Lista recuperada exitosamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DoctorDTO.class)))
    public ResponseEntity<GlobalResponse<List<DoctorDTO>>> getActiveDoctors() {
        List<DoctorDTO> data = doctorService.findAllActive();
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Lista de doctores activos"));
    }

    @GetMapping("/dashboard-paginated")
    @Operation(
            summary = "Listar todos los doctores (Paginado)",
            description = "Muestra el listado completo de doctores para el panel administrativo."
    )
    public ResponseEntity<GlobalResponse<Page<DoctorDTO>>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorDTO> data = doctorService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Lista de doctores paginada"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener detalle de un doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor encontrado"),
            @ApiResponse(responseCode = "404", description = "Doctor no encontrado", content = @Content)
    })
    public ResponseEntity<GlobalResponse<DoctorDTO>> getById(@PathVariable Long id) {
        DoctorDTO data = doctorService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Doctor encontrado"));
    }

    @PostMapping("/save")
    @Operation(
            summary = "Registrar nuevo doctor",
            description = "Crea el perfil del doctor **y automáticamente genera su usuario de acceso** (Login) con rol de DOCTOR."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Doctor y Usuario creados exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej: email repetido)", content = @Content)
    })
    public ResponseEntity<GlobalResponse<DoctorDTO>> saveDoctor(@Valid @RequestBody DoctorRequestDTO req) {
        DoctorDTO data = doctorService.save(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.success(data, "Doctor creado exitosamente"));
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualizar datos del doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Doctor actualizado"),
            @ApiResponse(responseCode = "404", description = "Doctor no encontrado", content = @Content)
    })
    public ResponseEntity<GlobalResponse<DoctorDTO>> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequestDTO req) {
        DoctorDTO data = doctorService.update(id, req);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(data, "Doctor actualizado"));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Eliminar doctor (Borrado Lógico)",
            description = "No elimina el registro de la BD para mantener la integridad de los turnos pasados. Solo cambia su estado a 'Inactivo' (`isActive = false`)."
    )
    public ResponseEntity<GlobalResponse<Void>> deleteDoctor(@PathVariable Long id) {
        doctorService.delete(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.success(null, "Doctor desactivado correctamente"));
    }
}
