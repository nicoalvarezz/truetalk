package com.fyp.hiveshared.api.responses;

import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Objects;

public class ResponseDeserializer {

    private String message;

    private HttpStatus method;

    private int status;

    private String service;

    private String timestamp;

    private Map<String, Object> data;


    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getMethod() {
        return method;
    }

    public void setMethod(HttpStatus method) {
        this.method = method;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseDeserializer that = (ResponseDeserializer) o;
        return status == that.status && Objects.equals(message, that.message) && method == that.method && Objects.equals(service, that.service) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, method, status, service, timestamp);
    }
}
