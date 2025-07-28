package com.seek.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO que representa las métricas de edad obtenidas de la base de datos.
 *  - averageAge: promedio de edades de todos los clientes
 *  - stdDevAge : desviación estándar de las edades
 */
@Data
@AllArgsConstructor
public class AgeMetricsDTO {

    private Double averageAge;
    private Double stdDevAge;
}
