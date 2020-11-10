package com.kay.security.validationcode;

/**
 * @author LiuKay
 * @since 2019/12/7
 */
public enum ValidationCodeType {

    SMS("smsCode"),
    IMAGE("imageCode");

    private String paramName;

    ValidationCodeType(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}
