package com.kay.security.validationcode.sms;

import com.kay.security.properties.SecurityProperties;
import com.kay.security.properties.SmsCodeProperties;
import com.kay.security.validationcode.VerificationCode;
import com.kay.security.validationcode.VerificationCodeGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.context.request.ServletWebRequest;

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
    public VerificationCode generate(ServletWebRequest request) {
        SmsCodeProperties sms = properties.getValidation().getSms();
        String randomNumeric = RandomStringUtils.randomNumeric(sms.getLength());
        return new VerificationCode(randomNumeric, sms.getExpire());
    }
}
