package com.ckiroshan.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
// Automatically returns HTTP 403 when this exception is thrown
public class ForbiddenException extends RuntimeException{
    // Exception thrown for Forbidden Exception
    public ForbiddenException(String message) {
        super(message);
    }
}