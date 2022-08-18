package com.github.kay.mmall.domain.security;

/**
 * OAuth2 授权类型
 **/
public interface GrantType {

    // 四种标准类型
    String PASSWORD = "password";
    String CLIENT_CREDENTIALS = "client_credentials";
    String IMPLICIT = "implicit";
    String AUTHORIZATIONCODE = "authorizationcode";
    // 用于刷新令牌
    String REFRESH_TOKEN = "refresh_token";

}
