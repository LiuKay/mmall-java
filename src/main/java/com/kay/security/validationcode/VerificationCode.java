package com.kay.security.validationcode;

import java.time.Duration;

import lombok.Getter;

@Getter
public class VerificationCode {

    private String code;
    private Duration duration;

    public VerificationCode(String code, int expiredSeconds) {
        this.code = code;
        this.duration = Duration.ofSeconds(expiredSeconds);
    }

}
