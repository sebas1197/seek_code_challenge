package com.seek.application.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO para exponer un cliente junto con la fecha estimada
 * de un evento futuro (p.ej. esperanza de vida).
 */
@Data
@AllArgsConstructor
public class CustomerWithEventDTO {

    private Long       id;
    private String     firstName;
    private String     lastName;
    private LocalDate  birthday;
    private LocalDate  estimatedEventDate;
}
