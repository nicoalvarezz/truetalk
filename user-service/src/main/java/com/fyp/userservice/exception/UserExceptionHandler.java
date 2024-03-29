package com.fyp.userservice.exception;

import com.fyp.hiveshared.api.responses.ResponseHandlers;
import com.fyp.hiveshared.api.responses.excpetion.NotFoundException;
import com.fyp.hiveshared.api.responses.excpetion.UnauthorizedException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;


@ControllerAdvice
public class UserExceptionHandler {

    private static final String SERVICE = "user-service";
    private static final String METHOD_ARGUMENT_ERROR = "Validation failed for some of the arguments. Make sure that all arguments are correct.";
    private static final String CONSTRAINT_VIOLATION_ERROR = "Unique constraint violated.";

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        return ResponseHandlers.responseBody(METHOD_ARGUMENT_ERROR, HttpStatus.BAD_REQUEST, SERVICE);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        return ResponseHandlers.responseBody(CONSTRAINT_VIOLATION_ERROR, HttpStatus.CONFLICT, SERVICE);
    }

    @ExceptionHandler({UnauthorizedException.class})
    protected ResponseEntity<Map<String, Object>> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        return ResponseHandlers.responseBody(ex.getMessage(), ex.getStatus(), SERVICE);
    }

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex, WebRequest request) {
        return ResponseHandlers.responseBody(ex.getMessage(), ex.getStatus(), SERVICE);
    }
}
