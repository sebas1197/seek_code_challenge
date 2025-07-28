package com.seek.domain.repository;

import java.util.List;
import java.util.Optional;

import com.seek.application.dto.AgeMetricsDTO;
import com.seek.application.dto.CustomerWithEventDTO;
import com.seek.domain.model.Customer;


public interface CustomerRepository {

    Customer save(Customer customer);
    void deleteById(Long id);
    Optional<Customer> findById(Long id);
    boolean existsById(Long id);
    List<Customer> findAll();
    AgeMetricsDTO fetchAgeMetrics();
    List<CustomerWithEventDTO> findAllWithEstimatedEvent();
}
