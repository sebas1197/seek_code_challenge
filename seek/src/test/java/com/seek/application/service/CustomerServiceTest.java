package com.seek.application.service;

import com.seek.application.dto.CustomerRequest;
import com.seek.application.dto.CustomerResponse;
import com.seek.application.mapper.CustomerMapper;
import com.seek.domain.model.Customer;
import com.seek.domain.repository.CustomerRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock  CustomerRepository repo;
    @Mock  CustomerMapper     mapper;
    @InjectMocks CustomerService service;

    private static CustomerRequest buildRequest() {
        CustomerRequest req = new CustomerRequest();
        req.setFirstName("Ana");
        req.setLastName("García");
        req.setBirthday(LocalDate.of(1990, 5, 1));
        return req;
    }

    private static Customer buildEntity() {
        Customer c = new Customer();
        c.setId(1L);
        c.setFirstName("Ana");
        c.setLastName("García");
        c.setBirthday(LocalDate.of(1990, 5, 1));
        return c;
    }

    private static CustomerResponse buildResponse() {
        return new CustomerResponse(
                "Ana", "García", 34,
                LocalDate.of(1990, 5, 1));
    }

    @Test
    void shouldRegisterCustomer() {
        CustomerRequest req  = buildRequest();
        Customer        ent  = buildEntity();
        CustomerResponse res = buildResponse();

        when(mapper.toEntity(req)).thenReturn(ent);
        when(repo.save(ent)).thenReturn(ent);
        when(mapper.toResponse(ent)).thenReturn(res);

        CustomerResponse result = service.register(req);

        assertEquals("Ana", result.getFirstName());
        verify(repo).save(ent);
    }

    @Test
    void shouldUpdateExistingCustomer() {
        CustomerRequest req = buildRequest();
        Customer        ent = buildEntity();

        when(repo.findById(1L)).thenReturn(Optional.of(ent));
        when(repo.save(ent)).thenReturn(ent);
        when(mapper.toResponse(ent)).thenReturn(buildResponse());

        CustomerResponse result = service.update(1L, req);

        assertEquals("Ana", result.getFirstName());
        verify(repo).findById(1L);
        verify(repo).save(ent);
    }

    @Test
    void shouldListAll() {
        Customer ent = buildEntity();
        when(repo.findAll()).thenReturn(List.of(ent));
        when(mapper.toResponse(ent)).thenReturn(buildResponse());

        List<CustomerResponse> list = service.listAll();

        assertEquals(1, list.size());
        verify(repo).findAll();
    }
}
