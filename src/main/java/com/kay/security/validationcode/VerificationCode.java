package com.kay.security.validationcode;

import java.time.LocalDateTime;

/**
 * @author LiuKay
 * @since 2019/12/7
 */
public class VerificationCode {

    private String code;
    private LocalDateTime expiredTime;

    public VerificationCode(String code, int expiredSeconds) {
        this.code = code;
        this.expiredTime = LocalDateTime.now().plusSeconds(expiredSeconds);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(LocalDateTime expiredTime) {
        this.expiredTime = expiredTime;
    }
}
