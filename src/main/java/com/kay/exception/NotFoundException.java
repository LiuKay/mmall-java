package com.kay.exception;

public class NotFoundException extends BaseException {

    private static final String CODE = "b646a4ef-336f-4a53-8c86-0298c3ff3fb9";

    public NotFoundException(String message) {
        super(message, CODE);
    }
}
