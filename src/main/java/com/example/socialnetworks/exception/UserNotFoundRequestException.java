package com.example.socialnetworks.exception;

public class UserNotFoundRequestException extends RuntimeException {

    public UserNotFoundRequestException(String message) {
        super(message);
    }

    public UserNotFoundRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
