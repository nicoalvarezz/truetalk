package com.fyp.hiveshared.api.responses.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class ServiceUnavailableException extends Exception {

    public ServiceUnavailableException(String message) {
        super(message);
    }
}
