package com.kay.security.properties;

/**
 * SMS verification code Config
 *
 * @author LiuKay
 * @since 2019/12/8
 */
public class SmsCodeProperties {
    private int length = 4;
    // seconds
    private int expire = 60;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

}
