package com.seek.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AgeMetricsDTO {

    private Double averageAge;
    private Double stdDevAge;
}
