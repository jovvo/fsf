package com.tlapps.test.fsf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FsfApiExceptionController {

    @ExceptionHandler(value = AuthorizationServiceException.class)
    public ResponseEntity<Object> exception(AuthorizationServiceException exception) {
        return new ResponseEntity<>("Unauthorized operation", HttpStatus.FORBIDDEN);
    }
}
