package com.seek.presentation.controller;

import com.seek.application.dto.AgeMetricsDTO;
import com.seek.application.dto.CustomerRequest;
import com.seek.application.dto.CustomerResponse;
import com.seek.application.dto.CustomerWithEventDTO;
import com.seek.application.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints REST para la gestión de clientes.
 *
 * <p>Todos los endpoints requieren JWT válido – la lógica de seguridad
 * se gestiona con {@code JwtAuthFilter}, por lo que aquí no se hace
 * verificación manual.</p>
 */
@Validated
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    /* ---------- Comandos ---------- */

    @PostMapping
    public ResponseEntity<CustomerResponse> create(
            @Valid @RequestBody CustomerRequest dto) {

        CustomerResponse response = service.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest dto) {

        CustomerResponse response = service.update(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    /* ---------- Consultas ---------- */

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/metrics")
    public ResponseEntity<AgeMetricsDTO> metrics() {
        return ResponseEntity.ok(service.metrics());
    }

    @GetMapping("/estimated-event")
    public ResponseEntity<List<CustomerWithEventDTO>> listWithEvent() {
        return ResponseEntity.ok(service.listWithEstimatedEvent());
    }
}
