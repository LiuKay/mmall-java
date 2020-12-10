package com.kay.security;

import com.kay.util.MD5Encoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author kay
 * @date 2019/9/6 22:11
 */
@Slf4j
public class MallPasswordEncoder implements PasswordEncoder {

    //TODO:move to config
    private static final String SLAT = "kaysdafaqj23ou89ZXcj@#$@#$#@KJdjklj;D../dSF.,";
    private static final MD5Encoder MD_5_ENCODER = new MD5Encoder(SLAT);

    @Override
    public String encode(CharSequence rawPassword) {
        return MD_5_ENCODER.md5EncodeUtf8(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() == 0) {
            log.warn("Empty encoded password");
            return false;
        }
        return StringUtils.equals(MD_5_ENCODER.md5EncodeUtf8(rawPassword.toString()), encodedPassword);
    }
}
