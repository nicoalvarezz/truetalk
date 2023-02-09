package com.fyp.hiveshared.api.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class ResponseHandlers {

    private static final String MESSAGE = "message";
    private static final String METHOD = "method";
    private static final String STATUS = "status";
    private static final String SERVICE = "service";
    private static final String TIMESTAMP = "timestamp";
    private static final String DATA = "data";

    public static ResponseEntity<Map<String, Object>> baseResponseBody(String message, HttpStatus status, String service) {
        return new ResponseEntity<>(new HashMap<>() {{
                put(MESSAGE, message);
                put(METHOD, status);
                put(STATUS, status.value());
                put(SERVICE, service);
                put(TIMESTAMP, ZonedDateTime.now(ZoneId.of("Z")).toString());
        }}, status);
    }


    public static ResponseEntity<Map<String, Object>> responseBodyWithData(String message, HttpStatus status, String service, Map<String, Object> data){
        return new ResponseEntity<>(new HashMap<>() {{
            put(MESSAGE, message);
            put(METHOD, status);
            put(STATUS, status.value());
            put(SERVICE, service);
            put(TIMESTAMP, ZonedDateTime.now(ZoneId.of("Z")).toString());
            put(DATA, data);
        }}, status);
    }
}
