package com.kay.security.properties;

/**
 * @author LiuKay
 * @since 2019/12/7
 */
public final class SecurityConstants {

    public static final String LOGIN_FORM_PROCESSING_URL = "/authentication/form";

    public static final String LOGIN_REQUIRE = "/authentication/require";

    public static final String VERIFICATION_CODE_URL = "/code/*";

    public static final String REQUEST_PARAMETER_MOBILE = "mobile";

    public static final String REQUEST_PARAMETER_SMS_CODE = "smsCode";


    public static final String LOGIN_MOBILE_PROCESSING_URL = "/authentication/mobile";

    private SecurityConstants() {
    }
}
