package com.kay.controller;

import com.kay.common.ServerResponse;
import com.kay.exception.NotFoundException;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ServerResponse.createByError(exception));
    }


    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity handleBadRequest(Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ServerResponse.createByError(exception));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity handleNotFound(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ServerResponse.createByError(exception));
    }
}
