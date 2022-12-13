package com.fyp.hiveshared.api.exception;

public class IdpalRequestException extends RuntimeException{
    public IdpalRequestException(String message) {
        super(message);
    }

    public IdpalRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
