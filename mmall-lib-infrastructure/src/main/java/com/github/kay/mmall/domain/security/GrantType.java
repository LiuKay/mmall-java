package com.github.kay.mmall.domain.security;

/**
 * OAuth2 授权类型
 **/
public enum GrantType {

    // 四种标准类型
    PASSWORD("password"),
    CLIENT_CREDENTIALS("client_credentials"),
    IMPLICIT("implicit"),
    AUTHORIZATION_CODE("authorization_code"),
    // 用于刷新令牌
    REFRESH_TOKEN("refresh_token");

    private final String value;

    GrantType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
