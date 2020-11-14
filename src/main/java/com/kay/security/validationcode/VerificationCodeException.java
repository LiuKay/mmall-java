package com.kay.security.validationcode;

import org.springframework.security.core.AuthenticationException;

/**
 * @author LiuKay
 * @since 2019/12/5
 */
public class VerificationCodeException extends AuthenticationException {

    public VerificationCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public VerificationCodeException(String msg) {
        super(msg);
    }
}
