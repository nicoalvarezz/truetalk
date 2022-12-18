package com.fyp.hiveshared.api.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ResponseHandler {

    public static ResponseEntity<Object> serviceResponse(String message, HttpStatus status, String service) {
        return new ResponseEntity<>(new HiveResponseBody(
                message,
                status.value(),
                status,
                service,
                ZonedDateTime.now(ZoneId.of("Z")).toString()
        ), status);
    }
}
