package com.seek.presentation.controller;

import com.seek.application.dto.AgeMetricsDTO;
import com.seek.application.dto.CustomerRequest;
import com.seek.application.dto.CustomerResponse;
import com.seek.application.dto.CustomerWithEventDTO;
import com.seek.application.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Customers", description = "Operaciones CRUD y métricas de clientes")
@Validated
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente con nombre, apellido y fecha de nacimiento.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cliente creado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CustomerResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
        @ApiResponse(responseCode = "422", description = "Regla de negocio violada", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest dto) {
        CustomerResponse response = service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente por ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cliente actualizado",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CustomerResponse.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest dto) {
        CustomerResponse response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente por su ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cliente eliminado"),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @Operation(summary = "Listar clientes", description = "Obtiene todos los clientes registrados.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de clientes",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CustomerResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @Operation(summary = "Métricas de edad", description = "Devuelve promedio y desviación estándar de las edades.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Métricas calculadas",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = AgeMetricsDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    @GetMapping("/metrics")
    public ResponseEntity<AgeMetricsDTO> metrics() {
        return ResponseEntity.ok(service.metrics());
    }

    @Operation(summary = "Fecha estimada de evento", description = "Lista clientes con fecha estimada de evento (p. ej. esperanza de vida).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista con fechas estimadas",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = CustomerWithEventDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    @GetMapping("/estimated-event")
    public ResponseEntity<List<CustomerWithEventDTO>> listWithEvent() {
        return ResponseEntity.ok(service.listWithEstimatedEvent());
    }
}
