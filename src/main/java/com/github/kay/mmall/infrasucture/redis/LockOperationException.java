package com.github.kay.mmall.infrasucture.redis;

public class LockOperationException extends RuntimeException{

    public LockOperationException(String message) {
        super(message);
    }

    public LockOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
