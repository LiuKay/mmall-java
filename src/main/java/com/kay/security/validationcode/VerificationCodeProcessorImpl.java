package com.kay.security.validationcode;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import com.kay.exception.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class VerificationCodeProcessorImpl implements VerificationCodeProcessor {

    @Autowired
    private VerificationCodeGenerator verificationCodeGenerator;

    @Autowired
    private SmsCodeSender smsCodeSender;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void send(String mobile) {
        VerificationCode code = verificationCodeGenerator.generate();
        store(mobile, code);
        smsCodeSender.send(mobile, code.getCode());
    }

    private void store(String mobile, VerificationCode code) {
        redisTemplate.boundValueOps(mobile).set(code.getCode(), code.getDuration());
    }

    @Override
    public void validate(String mobile, String codeToCompare) {
        Preconditions.checkNotNull(codeToCompare, "sms code can not be null.");
        String smsCode = redisTemplate.boundValueOps(mobile).get();
        if (Strings.isNullOrEmpty(smsCode)) {
            throw new NotFoundException(String.format("Not found sms code for mobile %s", mobile));
        }
        if (!smsCode.equals(codeToCompare)) {
            throw new IllegalArgumentException("Wrong sms code.");
        }
    }
}
