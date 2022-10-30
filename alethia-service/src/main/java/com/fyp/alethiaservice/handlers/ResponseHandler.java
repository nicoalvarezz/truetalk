package com.fyp.alethiaservice.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {

    public static ResponseEntity<Object> generateSimpleResponse(String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status_code", status.value());
        map.put("timestamp", ZonedDateTime.now(ZoneId.of("Z")).toString());

        return new ResponseEntity<>(map, status);
    }
}
