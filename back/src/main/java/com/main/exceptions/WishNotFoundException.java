package com.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * exception throw when a wish is not found into database
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND)  // 404
public class WishNotFoundException extends RuntimeException {

    public WishNotFoundException(String wishId) {
        super("No such Wish wish iId : " + wishId);
    }
}
