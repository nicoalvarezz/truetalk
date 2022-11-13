package com.fyp.alethiaservice.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new ApiException(
                "Validation failed for argument",
                status.value(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        ), status);
    }

    @ExceptionHandler({IdpalRequestException.class})
    public ResponseEntity<Object> handleIdpalRequestException(IdpalRequestException e) {
        return new ResponseEntity<>(new ApiException(
                e.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE,
                ZonedDateTime.now(ZoneId.of("Z"))
        ), HttpStatus.SERVICE_UNAVAILABLE);
    }
}


