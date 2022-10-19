package com.github.kay.mmall.infrasucture.lock;

public class LockOperationException extends RuntimeException{

    public LockOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockOperationException(Throwable cause) {
        super(cause);
    }
}
