package com.main.exceptions;


import com.main.webObject.ErrorDTO;
import org.springframework.dao.DuplicateKeyException;
import com.mongodb.MongoException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionsHandlers {

    @ResponseBody
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO alreadyPresentKey(HttpServletRequest req, Exception ex) {
        return new ErrorDTO(ex, req.getRequestURI(), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler(MongoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO jpaException(HttpServletRequest req, Exception ex) {
        return new ErrorDTO(ex, req.getRequestURI(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
