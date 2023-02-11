package com.fyp.hiveshared.api.responses.excpetion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    private HttpStatus status = HttpStatus.UNAUTHORIZED;

    public UnauthorizedException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
