package com.github.kay.mmall.security.provider;

import com.github.kay.mmall.infrasucture.security.JWTAccessToken;

import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

/**
 * 使用RSA SHA256私钥加密的JWT令牌
 * <p>
 * 为了便于对比，
 *
 * 生成jks私钥公钥等
 * keytool -genkeypair -alias mmall-rsa -keyalg RSA -keypass 123456 -keystore rsa.jks -storepass 123456 -validity 36500
 * keytool -list -rfc --keystore rsa.jks | openssl x509 -inform pem -pubkey > pubkey.txt
 *
 **/
@Service
@Primary
public class RSA256JWTAccessToken extends JWTAccessToken {

    private final JsonParser objectMapper = JsonParserFactory.create();

    RSA256JWTAccessToken(UserDetailsService userDetailsService) {
        super(userDetailsService);
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("rsa.jks"), "123456".toCharArray());
        setKeyPair(keyStoreKeyFactory.getKeyPair("mmall-rsa"));
    }

    /**
     * 增强令牌Header
     * 使用JWKS验证令牌时，Header中需要有kid（Key ID），设置Header的方法在Spring的默认实现中没有开放出来，这里添加个默认处理
     */
    @Override
    protected String encode(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Field signerField = ReflectionUtils.findField(this.getClass(), "signer");
        signerField.setAccessible(true);
        Signer signer = (Signer) ReflectionUtils.getField(signerField, this);

        String content;
        try {
            content = this.objectMapper.formatMap(getAccessTokenConverter().convertAccessToken(accessToken, authentication));
        } catch (Exception ex) {
            throw new IllegalStateException("Cannot convert access token to JSON", ex);
        }

        Map<String, String> headers = Collections.singletonMap("kid", "mmall-jwt-kid");
        return JwtHelper.encode(content, signer, headers).getEncoded();
    }
}
