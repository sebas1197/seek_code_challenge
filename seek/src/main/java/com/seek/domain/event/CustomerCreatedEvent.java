
package com.seek.domain.event;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerCreatedEvent implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
}
