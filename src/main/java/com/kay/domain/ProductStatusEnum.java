package com.kay.domain;

public enum ProductStatusEnum {
    ON_SALE(1, "在售");

    private int code;
    private String value;

    ProductStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
