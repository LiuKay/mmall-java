package com.kay.security.validationcode;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author LiuKay
 * @since 2019/12/6
 */
public interface VerificationCodeGenerator {
    VerificationCode generate(ServletWebRequest request);
}
