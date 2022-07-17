package com.github.kay.mmall.infrasucture.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler({
            ConstraintViolationException.class
    })
    public ResponseEntity<CodedMessage> badRequest(ConstraintViolationException exception) {

        log.warn("validation failed.", exception);

        return CommonResponse.send(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CodedMessage> notFound(ResourceNotFoundException notFoundException){
        return CommonResponse.send(HttpStatus.NOT_FOUND, notFoundException.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CodedMessage> error(Exception exception){

        log.error("internal error.", exception);

        String message = exception.getMessage();
        message = StringUtils.isEmpty(message) ? exception.toString() : message;

        return CommonResponse.send(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}
