package com.fyp.alethiaservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public HttpStatus getError() {
        return error;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }
}
