package com.fyp.userservice.exception;

import com.fyp.hiveshared.api.responses.HiveResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class UserExceptionHandler {

    private static final String USER_SERVICE = "user-service";
    private static final String METHOD_ARGUMENT_ERROR = "Validation failed for some of the arguments. Make sure that all arguments are correct";

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        return new ResponseEntity<>(new HiveResponseBody(
                METHOD_ARGUMENT_ERROR,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST,
                USER_SERVICE,
                ZonedDateTime.now(ZoneId.of("Z")).toString()
        ), HttpStatus.BAD_REQUEST);
    }
}
