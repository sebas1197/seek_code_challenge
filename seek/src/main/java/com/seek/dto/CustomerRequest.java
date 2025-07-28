package com.seek.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CustomerRequest {
    private String name;
    private String lastname;
    private LocalDate birthday;
}
