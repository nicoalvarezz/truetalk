package com.fyp.hiveshared.api.responses.excpetion;

import com.fyp.hiveshared.api.responses.ResponseBody;
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

    private static final String ALETHIA_SERVICE = "alethia-service";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(new ResponseBody(
                "Validation failed for some of the arguments. Make sure that all arguments are correct",
                status.value(),
                status,
                ZonedDateTime.now(ZoneId.of("Z")).toString()
        ), status);
    }

    @ExceptionHandler({IdpalRequestException.class})
    public ResponseEntity<Object> handleIdpalRequestException(IdpalRequestException e) {
        return new ResponseEntity<>(new ResponseBody(
                e.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                HttpStatus.SERVICE_UNAVAILABLE,
                ALETHIA_SERVICE,
                ZonedDateTime.now(ZoneId.of("Z")).toString()
        ), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
