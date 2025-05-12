package com.ckiroshan.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
// Automatically returns HTTP 404 when this exception is thrown
public class ResourceNotFoundException extends RuntimeException {
    // Exception thrown for requested resource not found
    public ResourceNotFoundException(String message) {
        super(message);
    }
}