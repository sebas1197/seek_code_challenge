package com.seek.domain.repository;

import java.util.List;
import java.util.Optional;

import com.seek.application.dto.AgeMetricsDTO;
import com.seek.application.dto.CustomerWithEventDTO;
import com.seek.domain.model.Customer;

/**
 * Puerto de persistencia para la entidad {@link Customer}.
 *
 * <p>Este contrato pertenece a la capa <b>Dominio</b>; por tanto,
 * no tiene dependencias con Spring ni con ningún framework.</p>
 *
 * <p>La implementación concreta (adaptador) se encuentra en la capa
 * <i>infrastructure.persistence</i> —por ejemplo,
 * {@code JpaCustomerRepository}—, la cual sí utiliza Spring Data JPA.</p>
 */
public interface CustomerRepository {

    /* ---------- Comandos ---------- */

    Customer save(Customer customer);

    void deleteById(Long id);

    /* ---------- Consultas ---------- */

    Optional<Customer> findById(Long id);

    boolean existsById(Long id);

    List<Customer> findAll();

    AgeMetricsDTO fetchAgeMetrics();

    List<CustomerWithEventDTO> findAllWithEstimatedEvent();
}
