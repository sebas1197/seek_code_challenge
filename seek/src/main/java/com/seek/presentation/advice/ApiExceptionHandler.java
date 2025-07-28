package com.seek.presentation.advice;

import com.seek.domain.exception.BusinessValidationException;
import com.seek.domain.exception.CustomerNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleNotFound(CustomerNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage());
    }

    @ExceptionHandler(BusinessValidationException.class)
    public ProblemDetail handleBusinessRule(BusinessValidationException ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_ENTITY,
                ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {

        String detail = ex.getBindingResult()
                          .getFieldErrors()
                          .stream()
                          .map(err -> err.getField() + ": " + err.getDefaultMessage())
                          .collect(Collectors.joining(", "));

        return ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                detail);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        return ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error: " + ex.getMessage());
    }
}
