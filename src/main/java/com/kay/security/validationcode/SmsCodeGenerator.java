package com.kay.security.validationcode;

import com.kay.security.properties.SecurityProperties;
import com.kay.security.properties.SmsCodeProperties;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * SMS verification code Generator
 *
 * @author LiuKay
 * @since 2019/12/7
 */
public class SmsCodeGenerator implements VerificationCodeGenerator {

    private final SecurityProperties properties;

    public SmsCodeGenerator(SecurityProperties properties) {
        this.properties = properties;
    }

    @Override
    public VerificationCode generate() {
        SmsCodeProperties sms = properties.getValidation().getSms();
        String randomNumeric = RandomStringUtils.randomNumeric(sms.getLength());
        return new VerificationCode(randomNumeric, sms.getExpire());
    }
}
