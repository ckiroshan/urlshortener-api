package com.ckiroshan.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Enables global exception handling for REST controllers
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    // Handles validation errors when request body fields fail @Valid constraints.
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        // Returns a map of field names & their corresponding error messages.
        return ResponseEntity.badRequest().body(errors);
    }

    // Handles custom Bad Request Exceptions
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),  // HTTP status code (400)
                ex.getMessage(),                // Error message from exception
                System.currentTimeMillis()     // Timestamp of error
        );
        // Returns a structured error response.
        return ResponseEntity.badRequest().body(error);
    }

    // Handles custom Resource Not Found Exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),    // HTTP status code (404)
                ex.getMessage(),                // Error message from exception
                System.currentTimeMillis()     // Timestamp of error
        );
        // Returns a structured error response.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // Handles custom Username Not Found Exceptions
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),    // HTTP status code (401)
                ex.getMessage(),                   // Error message from exception
                System.currentTimeMillis()        // Timestamp of error
        );
        // Returns a structured error response
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // Handles custom Authentication Exceptions
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),    // HTTP status code (401)
                ex.getMessage(),                   // Error message from exception
                System.currentTimeMillis()        // Timestamp of error
        );
        // Returns a structured error response
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
