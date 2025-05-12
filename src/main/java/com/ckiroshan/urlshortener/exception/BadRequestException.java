package com.ckiroshan.urlshortener.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
// Automatically returns HTTP 400 when this exception is thrown
public class BadRequestException extends RuntimeException {
    // Exception thrown for invalid requests,
    //  e.g., duplicate custom short codes.
    public BadRequestException(String message) {
        super(message);
    }
}
