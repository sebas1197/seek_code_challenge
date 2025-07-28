package com.seek.domain.exception;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(Long id) {
        super("Customer with id " + id + " not found.");
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}
