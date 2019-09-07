package com.kay.config;

import com.kay.util.MD5Util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author kay
 * @date 2019/9/6 22:11
 */
@Slf4j
public class MmallPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.MD5EncodeUtf8(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() == 0) {
            log.warn("Empty encoded password");
            return false;
        }
        return StringUtils.equals(MD5Util.MD5EncodeUtf8(rawPassword.toString()), encodedPassword);
    }
}
