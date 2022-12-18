package com.fyp.hiveshared.api.responses.excpetion;

import com.fyp.hiveshared.api.responses.HiveResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ExceptionHandlerResponses {

    public static ResponseEntity<Object> exceptionHandlerResponse(String message, HttpStatus status, String service) {
        return new ResponseEntity<>(new HiveResponseBody(
                message,
                status.value(),
                status,
                service,
                ZonedDateTime.now(ZoneId.of("Z")).toString()
        ), status);
    }
}
