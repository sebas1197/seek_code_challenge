package com.seek.application.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerResponse {
    private String name;
    private String lastname;
    private int age;
    private LocalDate birthday;
    private String message;
}
