package com.fyp.hiveshared.api.responses;

import org.springframework.http.HttpStatus;

public class ResponseBody {
    private String message;
    private int status;
    private HttpStatus method;
    private String service;
    private String timestamp;

    public ResponseBody() {

    }

    public ResponseBody(String message, int status, HttpStatus method, String service, String timestamp) {
        this.message = message;
        this.status = status;
        this.method = method;
        this.service = service;
        this.timestamp = timestamp;
    }

    public ResponseBody(String message, int status, HttpStatus method, String timestamp) {
        this.message = message;
        this.status = status;
        this.method = method;
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
