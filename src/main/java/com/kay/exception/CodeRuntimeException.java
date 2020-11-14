package com.kay.exception;

public class CodeRuntimeException extends RuntimeException {

    private String code;

    public CodeRuntimeException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
