package com.kay.exception;

public class BaseException extends RuntimeException {

    private String code;

    public BaseException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BaseException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public BaseException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
