package com.kay.common;

/**
 * Created by kay on 2018/3/19.
 */
public enum ResponseCode {

    SUCCESS(0, "SUCCESS"),

    ERROR(-1, "UNKNOWN ERROR"),

    /* user related 1001- 1999 */
    NEED_LOGIN(1001, "NEED_LOGIN"),

    /* parameter related 2001-2999 */
    ILLEGAL_ARGUMENT(2001, "ILLEGAL_ARGUMENT");

    private int code;
    private String description;

    ResponseCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
