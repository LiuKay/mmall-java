package com.kay.domain;

public enum PaymentTypeEnum {
    ONLINE_PAY(1, "在线支付");

    PaymentTypeEnum(int code, String value) {
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


    public static PaymentTypeEnum codeOf(int code) {
        for (PaymentTypeEnum paymentTypeEnum : values()) {
            if (paymentTypeEnum.getCode() == code) {
                return paymentTypeEnum;
            }
        }
        throw new RuntimeException("么有找到对应的枚举");
    }
}
