package com.seek.application.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository repo;
    private final CustomerMapper mapper;
    private final CustomerEventProducer eventProducer;

    public CustomerResponse register(CustomerRequest req) {
        validateBirthday(req.getBirthday());

        Customer saved = repo.save(mapper.toEntity(req));

        eventProducer.send(new CustomerCreatedEvent(
                saved.getId(), saved.getFirstName(),
                saved.getLastName(), saved.getBirthday()));

        return mapper.toResponse(saved);
    }

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

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> listAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CustomerWithEventDTO> listWithEstimatedEvent() {
        return repo.findAllWithEstimatedEvent();
    }

    @Transactional(readOnly = true)
    public AgeMetricsDTO metrics() {
        return repo.fetchAgeMetrics();
    }

    private void validateBirthday(LocalDate birthday) {
        if (birthday.isAfter(LocalDate.now())) {
            throw new BusinessValidationException("Birthday cannot be in the future.");
        }
    }
}
