package com.seek.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.seek.application.dto.AgeMetricsDTO;
import com.seek.application.dto.CustomerWithEventDTO;
import com.seek.domain.model.Customer;
import com.seek.domain.repository.CustomerRepository;

@Repository
public interface JpaCustomerRepository
                extends JpaRepository<Customer, Long>,
                CustomerRepository { // «puerto» del dominio
        /* JPQL de métricas */
        @Query("""
                            SELECT new com.seek.application.dto.AgeMetricsDTO(
                                    AVG(c.birthday), STDDEV(c.birthday))
                            FROM Customer c
                        """)
        AgeMetricsDTO fetchAgeMetrics();

        /* Proyección con fecha estimada */
        @Query("""
                            SELECT new com.seek.application.dto.CustomerWithEventDTO(
                                    c.id, c.firstName, c.lastName, c.birthday,
                                    FUNCTION('DATE_ADD', c.birthday, 80, 'YEAR'))
                            FROM Customer c
                        """)
        List<CustomerWithEventDTO> findAllWithEstimatedEvent();
}
