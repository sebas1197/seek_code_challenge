package com.seek.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class CustomerRequest {
    @NotBlank @Size(max = 50)
    String firstName;
    @NotBlank @Size(max = 50)
    String lastName;
    @Past LocalDate birthday;
}

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    Customer toEntity(CustomerRequest dto);
    CustomerResponse toResponse(Customer entity);
}