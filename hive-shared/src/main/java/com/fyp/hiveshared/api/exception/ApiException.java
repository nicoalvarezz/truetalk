package com.fyp.hiveshared.api.exception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ApiException {
    private final String message;
    private final int status;
    private final HttpStatus error;
    private final ZonedDateTime timestamp;

    public ApiException(String message, int status, HttpStatus error, ZonedDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.timestamp = timestamp;
    }
}
