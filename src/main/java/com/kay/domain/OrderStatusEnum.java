package com.kay.domain;

public enum OrderStatusEnum {
    CANCEL(0, "已取消"),
    NO_PAY(10, "未支付"),
    PAID(20, "已付款"),
    SHIPPED(40, "已发货"),
    ORDER_SUCCESS(50, "订单完成"),
    ORDER_CLOSE(60, "订单关闭");

    private String value;
    private int code;

    OrderStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static OrderStatusEnum codeOf(int code) {
        for (OrderStatusEnum orderStatusEnum : values()) {
            if (orderStatusEnum.getCode() == code) {
                return orderStatusEnum;
            }
        }
        throw new RuntimeException("么有找到对应的枚举");
    }

}
