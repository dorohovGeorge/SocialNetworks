package com.example.socialnetworks.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class UserNotFoundHandler {
    @ExceptionHandler(value = {UserNotFoundRequestException.class})
    public ResponseEntity<Object> handleNotFoundException(UserNotFoundRequestException e) {
        HttpStatus notFoundRequest = HttpStatus.NOT_FOUND;
        UserNotFoundException userNotFoundException = new UserNotFoundException(e.getMessage(),
                                                                                notFoundRequest,
                                                                                ZonedDateTime.now(ZoneId.of("Z")));
        return new ResponseEntity<>(userNotFoundException, notFoundRequest);
    }
}
