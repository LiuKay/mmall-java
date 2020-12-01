package com.kay.domain;

public enum PayPlatformEnum {
    ALIPAY(1, "支付宝");

    PayPlatformEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    private String value;
    private int code;

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
}
