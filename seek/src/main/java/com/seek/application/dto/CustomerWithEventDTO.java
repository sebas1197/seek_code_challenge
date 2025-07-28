package com.seek.application.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerWithEventDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private LocalDate estimatedEventDate;
}
