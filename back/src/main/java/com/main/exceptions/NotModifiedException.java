package com.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * exception throw when an user is not found into database
 */
@ResponseStatus(code = HttpStatus.NOT_MODIFIED)  // 304
public class NotModifiedException extends RuntimeException {

    public NotModifiedException(Object resourceNotModified) {
        super("this resource has not been modified: " + resourceNotModified.toString());
    }
}
