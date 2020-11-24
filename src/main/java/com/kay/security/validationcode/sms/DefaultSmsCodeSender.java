package com.kay.security.validationcode.sms;


import lombok.extern.slf4j.Slf4j;

/**
 * Default implementation of SmsCodeSender only for debug
 *
 * @author LiuKay
 * @since 2019/12/7
 */
@Slf4j
public class DefaultSmsCodeSender implements SmsCodeSender {

    @Override
    public void send(String mobile, String code) {
        log.warn("Mock sending a sms code {} to mobile {}.", code, mobile);
    }
}
