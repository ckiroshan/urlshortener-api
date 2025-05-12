package com.ckiroshan.urlshortener.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

// Generic DTO for consistent error response format
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
}
