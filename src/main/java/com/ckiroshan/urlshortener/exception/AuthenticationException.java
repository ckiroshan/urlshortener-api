package com.ckiroshan.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
// Automatically returns HTTP 401 when this exception is thrown
public class AuthenticationException extends RuntimeException {
    // Exception thrown for Authentication Exception
    public AuthenticationException(String message) {
        super(message);
    }
}