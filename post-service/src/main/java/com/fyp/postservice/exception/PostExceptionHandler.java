package com.fyp.postservice.exception;

import com.fyp.hiveshared.api.responses.ResponseHandlers;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@ControllerAdvice
public class PostExceptionHandler {

    private static final String SERVICE = "post-service";
    private static final String METHOD_ARGUMENT_ERROR = "Sorry, the method argument provided is not valid. Please check your input and try again.";

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Map<String, Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, WebRequest request) {
        return ResponseHandlers.baseResponseBody(METHOD_ARGUMENT_ERROR, HttpStatus.BAD_REQUEST, SERVICE);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Map<String, Object>> handleIllegalArgumentException() {
        return ResponseHandlers.baseResponseBody(METHOD_ARGUMENT_ERROR, HttpStatus.BAD_REQUEST, SERVICE);
    }
}
