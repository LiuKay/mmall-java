package com.kay.security.validationcode;

/**
 * @author LiuKay
 * @since 2019/12/7
 */
public interface SmsCodeSender {

    /**
     * Send sms code
     *
     * @param mobile target mobile number
     * @param code   sms verification code
     */
    void send(String mobile, String code);
}
