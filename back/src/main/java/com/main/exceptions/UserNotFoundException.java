package com.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * exception throw when an user is not found into database
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)  // 404
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userId) {
        super("No such User user id : " + userId);
    }
}
