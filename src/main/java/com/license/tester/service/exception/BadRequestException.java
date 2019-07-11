package com.license.tester.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "An unexpected error occured, please verify the submitted data.")
public class BadRequestException extends RuntimeException {
}
