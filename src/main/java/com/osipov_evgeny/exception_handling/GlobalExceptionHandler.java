package com.osipov_evgeny.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionTemplate> handleException(Exception exception) {
        ExceptionTemplate exceptionTemplate = new ExceptionTemplate(exception.getMessage());

        return new ResponseEntity<>(exceptionTemplate, HttpStatus.BAD_REQUEST);
    }
}
