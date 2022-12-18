package com.fyp.alethiaservice.exceptions;

import com.fyp.hiveshared.api.responses.HiveResponseBody;
import com.fyp.hiveshared.api.responses.excpetion.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@ControllerAdvice
public class AlethiaExceptionHandler {

    private static final String METHOD_ARGUMENT_ERROR = "Validation failed for some of the arguments. Make sure that all arguments are correct";
    private static final String ALETHIA_SERVICE = "alethia-service";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        return new ResponseEntity<>(new HiveResponseBody(
                METHOD_ARGUMENT_ERROR,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                ALETHIA_SERVICE,
                ZonedDateTime.now(ZoneId.of("Z")).toString()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<?> handleServiceUnavailableException(ServiceUnavailableException ex, WebRequest request) {
        return new ResponseEntity<>(new HiveResponseBody(
                ex.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE,
                ALETHIA_SERVICE,
                ZonedDateTime.now(ZoneId.of("Z")).toString()
        ), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
