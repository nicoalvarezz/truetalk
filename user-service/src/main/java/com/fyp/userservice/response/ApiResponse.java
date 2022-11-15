package com.fyp.userservice.response;

import org.springframework.http.HttpStatus;

public class ApiResponse {
    private String message;
    private int status;
    private HttpStatus method;
    private String service;
    private String timestamp;

    public ApiResponse() {

    }

    public ApiResponse(String message, int status, HttpStatus method, String service, String timestamp) {
        this.message = message;
        this.status = status;
        this.method = method;
        this.service = service;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public HttpStatus getMethod() {
        return method;
    }

    public String getService() {
        return service;
    }

    public String getTimestamp() {
        return timestamp;
    }
}