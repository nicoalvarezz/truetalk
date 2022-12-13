package com.fyp.hiveshared.api.responses.excpetion;

public class IdpalRequestException extends RuntimeException{
    public IdpalRequestException(String message) {
        super(message);
    }

    public IdpalRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
