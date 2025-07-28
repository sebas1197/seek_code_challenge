package com.seek.presentation.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seek.application.dto.CustomerRequest;
import com.seek.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class CustomerApiIT {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.3")
            .withDatabaseName("customerdb")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    static void mysqlProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url",      mysql::getJdbcUrl);
        r.add("spring.datasource.username", mysql::getUsername);
        r.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired MockMvc      mvc;
    @Autowired JwtService   jwtService;
    @Autowired ObjectMapper mapper;

    private String jwt;

    @BeforeEach
    void setUp() {
        jwt = "Bearer " + jwtService.createToken("tester@example.com");
    }

    @Test
    void createCustomerReturns201() throws Exception {
        CustomerRequest dto = new CustomerRequest();
        dto.setFirstName("Juan");
        dto.setLastName("Pérez");
        dto.setBirthday(LocalDate.of(1985, 3, 20));

        mvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", jwt)
                .content(mapper.writeValueAsString(dto)))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.firstName").value("Juan"))
           .andExpect(jsonPath("$.age").exists());
    }

    @Test
    void createCustomerValidationErrorReturns400() throws Exception {
        CustomerRequest dto = new CustomerRequest(); // campos vacíos

        mvc.perform(post("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", jwt)
                .content(mapper.writeValueAsString(dto)))
           .andExpect(status().isBadRequest());
    }
}
