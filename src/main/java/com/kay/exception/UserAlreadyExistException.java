package com.kay.exception;

public class UserAlreadyExistException extends BaseException {

    private static final String CODE = "1f8c038d-c119-4eae-9b78-9b48c82c75e5";

    public UserAlreadyExistException(String message) {
        super(message, CODE);
    }
}
