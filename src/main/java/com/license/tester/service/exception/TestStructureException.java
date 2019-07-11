package com.license.tester.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TestStructureException extends RuntimeException {

    public TestStructureException(String message) {
        super(message);
    }

    public TestStructureException(String message, Throwable cause) {
        super(message, cause);
    }
}
