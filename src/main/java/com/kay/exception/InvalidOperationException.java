package com.kay.exception;

public class InvalidOperationException extends BaseException {

    private static final String CODE = "TODO:code";

    public InvalidOperationException(String message) {
        super(message, CODE);
    }
}
