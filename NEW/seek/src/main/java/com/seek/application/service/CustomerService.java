package com.seek.application.service;

import com.seek.application.dto.AgeMetricsDTO;
import com.seek.application.dto.CustomerRequest;
import com.seek.application.dto.CustomerResponse;
import com.seek.application.dto.CustomerWithEventDTO;
import com.seek.application.mapper.CustomerMapper;
import com.seek.domain.event.CustomerCreatedEvent;
import com.seek.domain.exception.BusinessValidationException;
import com.seek.domain.exception.CustomerNotFoundException;
import com.seek.domain.model.Customer;
import com.seek.domain.repository.CustomerRepository;
import com.seek.infrastructure.messaging.CustomerEventProducer;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Caso de uso principal para la gestión de clientes.
 * Mantiene la lógica de negocio aislada de la infraestructura
 * y se apoya en puertos (repositorios) y mappers para
 * persistencia y transformación de datos.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository repo;
    private final CustomerMapper mapper;
    private final CustomerEventProducer eventProducer;

    /* ---------- Comandos ---------- */

    /** Crea un nuevo cliente y devuelve su representación de respuesta. */

    public CustomerResponse register(CustomerRequest req) {
        validateBirthday(req.getBirthday());

        Customer saved = repo.save(mapper.toEntity(req));

        // NEW: publicar evento
        eventProducer.send(new CustomerCreatedEvent(
                saved.getId(), saved.getFirstName(),
                saved.getLastName(), saved.getBirthday()));

        return mapper.toResponse(saved);
    }

    /** Actualiza datos de un cliente existente. */
    public CustomerResponse update(Long id, CustomerRequest req) {
        validateBirthday(req.getBirthday());

        Customer entity = repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        entity.setFirstName(req.getFirstName());
        entity.setLastName(req.getLastName());
        entity.setBirthday(req.getBirthday());

        Customer updated = repo.save(entity);
        return mapper.toResponse(updated);
    }

    /** Elimina un cliente por ID. */
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        repo.deleteById(id);
    }

    /* ---------- Consultas ---------- */

    /** Devuelve todos los clientes mapeados a DTO de respuesta. */
    @Transactional(readOnly = true)
    public List<CustomerResponse> listAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    /** Devuelve clientes con fecha estimada de evento (p.ej. esperanza de vida). */
    @Transactional(readOnly = true)
    public List<CustomerWithEventDTO> listWithEstimatedEvent() {
        return repo.findAllWithEstimatedEvent();
    }

    /** Calcula métricas de edad (promedio y desviación estándar). */
    @Transactional(readOnly = true)
    public AgeMetricsDTO metrics() {
        return repo.fetchAgeMetrics();
    }

    /* ---------- Helpers ---------- */

    /** Valida que la fecha de nacimiento no sea futura. */
    private void validateBirthday(LocalDate birthday) {
        if (birthday.isAfter(LocalDate.now())) {
            throw new BusinessValidationException("Birthday cannot be in the future.");
        }
    }
}
