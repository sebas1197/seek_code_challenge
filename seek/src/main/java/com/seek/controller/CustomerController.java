package com.seek.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seek.dto.AgeMetrics;
import com.seek.dto.CustomerEventProjection;
import com.seek.dto.CustomerRequest;
import com.seek.dto.CustomerResponse;
import com.seek.model.Customer;
import com.seek.service.CustomerService;
import com.seek.util.JWTUtil;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    private final JWTUtil jWUtil = new JWTUtil();

    @PostMapping("/create")
    public ResponseEntity create(
            @RequestBody CustomerRequest customerRequest,
            @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jWUtil.isTokenValid(jwt)) {
                return ResponseEntity
                        .badRequest()
                        .body("Invalid JWT");
            }

            CustomerResponse customerResponse;
            customerResponse = this.customerService.create(customerRequest);
            return ResponseEntity.ok(customerResponse);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("The product could not be created, "
                            + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity listCustomers(
            @RequestHeader("Authorization") String jwt) {

        try {

            if (!this.jWUtil.isTokenValid(jwt)) {
                return ResponseEntity
                        .badRequest()
                        .body("Invalid JWT");
            }

            List<Customer> customers;
            customers = this.customerService.listCustomers();
            return ResponseEntity.ok(customers);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Could not list customers, "
                            + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(
            @PathVariable Long id,
            @RequestBody CustomerRequest customerRequest,
            @RequestHeader("Authorization") String jwt) {

        try {

            if (!this.jWUtil.isTokenValid(jwt)) {
                return ResponseEntity
                        .badRequest()
                        .body("Invalid JWT");
            }

            CustomerResponse customerResponse;
            customerResponse = this.customerService.update(
                    id, customerRequest);

            return ResponseEntity.ok(customerResponse);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("The product could not be updated, "
                            + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt) {
        try {

            if (!this.jWUtil.isTokenValid(jwt)) {
                return ResponseEntity
                        .badRequest()
                        .body("Invalid JWT");
            }

            String message;
            message = this.customerService.delete(id);
            return ResponseEntity.ok(message);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("The customer could not be deleted, "
                            + e.getMessage());
        }
    }

    @GetMapping("/metrics")
    public ResponseEntity<?> getAgeMetrics(
            @RequestHeader("Authorization") String jwt) {
        try {
            if (!jWUtil.isTokenValid(jwt)) {
                return ResponseEntity
                        .badRequest()
                        .body("Invalid JWT");
            }
            AgeMetrics metrics = customerService.getAgeMetrics();
            return ResponseEntity.ok(metrics);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Could not retrieve age metrics: " + e.getMessage());
        }
    }

    public ResponseEntity<?> listWithEstimatedEventDate(
            @RequestHeader("Authorization") String jwt) {
        try {
            if (!jWUtil.isTokenValid(jwt)) {
                return ResponseEntity
                        .badRequest()
                        .body("Invalid JWT");
            }
            List<CustomerEventProjection> list = customerService.listCustomersWithEstimatedEventDate();
            return ResponseEntity.ok(list);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Could not list customers with estimated event date: " + e.getMessage());
        }
    }

}
