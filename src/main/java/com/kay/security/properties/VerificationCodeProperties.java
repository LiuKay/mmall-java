package com.kay.security.properties;

import lombok.Data;

/**
 * @author LiuKay
 * @since 2019/12/6
 */
@Data
public class VerificationCodeProperties {
    private SmsCodeProperties sms = new SmsCodeProperties();
}
