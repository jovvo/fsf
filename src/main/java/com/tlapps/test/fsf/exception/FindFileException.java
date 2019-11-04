package com.tlapps.test.fsf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FindFileException extends RuntimeException {
    public FindFileException(String message) {
        super(message);
    }

    public FindFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
