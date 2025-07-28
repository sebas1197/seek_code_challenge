package com.seek.application.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CustomerResponse {

    private String  firstName;
    private String  lastName;
    private int     age;
    private LocalDate birthday;
    private String  message;
}
