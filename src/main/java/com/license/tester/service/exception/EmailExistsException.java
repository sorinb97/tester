package com.license.tester.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "An account is already registered with the same email.")
public class EmailExistsException extends RuntimeException {
}
