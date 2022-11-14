package com.fyp.alethiaservice.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ResponseHandler {

    private static final String SERVICE = "alethia-service";

    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(new ApiResponse(
                message,
                status.value(),
                status,
                SERVICE,
                ZonedDateTime.now(ZoneId.of("Z")).toString()
        ), status);
    }
}
