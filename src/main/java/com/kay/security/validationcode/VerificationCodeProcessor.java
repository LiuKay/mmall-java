package com.kay.security.validationcode;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author LiuKay
 * @since 2019/12/7
 */
public interface VerificationCodeProcessor {

    /**
     * create a verification code
     *
     * @param request
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * validate the code in the request
     *
     * @param request
     */
    void validate(ServletWebRequest request);


    /**
     * Send verificationCode
     *
     * @param request
     * @param verificationCode
     * @throws Exception
     */
    void send(ServletWebRequest request, Object verificationCode) throws Exception;
}
