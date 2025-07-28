package com.seek.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.seek.dto.AgeMetrics;
import com.seek.dto.CustomerEventProjection;
import com.seek.model.Customer;

@Repository
public class CustomerRepository extends JpaRepository<Customer, Long> {
        @Query(value = "SELECT AVG(age)       AS averageAge, " +
                        "       STDDEV_POP(age) AS stdDevAge " +
                        "  FROM customers", nativeQuery = true)
        AgeMetrics getAgeMetrics();

        @Query(value = "SELECT c.id                  AS id, " +
                        "       c.first_name         AS firstName, " +
                        "       c.last_name          AS lastName, " +
                        "       c.age                AS age, " +
                        "       c.birthday           AS birthday, " +
                        "       DATE_ADD(c.birthday, INTERVAL 80 YEAR) " +
                        "         AS estimatedEventDate " +
                        "  FROM customers c", nativeQuery = true)
        List<CustomerEventProjection> findAllWithEstimatedEventDate();
}
