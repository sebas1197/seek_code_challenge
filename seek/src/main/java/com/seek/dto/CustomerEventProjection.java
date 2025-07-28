package com.seek.dto;

import java.time.LocalDate;

public interface CustomerEventProjection {
    Long getId();

    String getFirstName();

    String getLastName();

    Integer getAge();

    LocalDate getBirthday();

    LocalDate getEstimatedEventDate();
}