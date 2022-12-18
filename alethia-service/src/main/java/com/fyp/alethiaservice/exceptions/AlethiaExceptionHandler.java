package com.fyp.alethiaservice.exceptions;

import com.fyp.hiveshared.api.responses.excpetion.ExceptionHandlerResponses;
import com.fyp.hiveshared.api.responses.excpetion.ServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;



@ControllerAdvice
public class AlethiaExceptionHandler {

    private static final String SERVICE = "alethia-service";
    private static final String METHOD_ARGUMENT_ERROR = "Validation failed for some of the arguments. Make sure that all arguments are correct";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        return ExceptionHandlerResponses.exceptionHandlerResponse(METHOD_ARGUMENT_ERROR, HttpStatus.BAD_REQUEST, SERVICE);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<?> handleServiceUnavailableException(ServiceUnavailableException ex, WebRequest request) {
        return ExceptionHandlerResponses.exceptionHandlerResponse(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE, SERVICE);
    }
}
