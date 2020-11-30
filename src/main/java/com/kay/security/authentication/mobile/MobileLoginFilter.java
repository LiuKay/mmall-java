package com.kay.security.authentication.mobile;

import com.kay.security.properties.SecurityConstants;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class MobileLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String MOBILE_PARAMETER = SecurityConstants.REQUEST_PARAMETER_MOBILE;
    private static final String SMS_CODE_PARAMETER = SecurityConstants.REQUEST_PARAMETER_SMS_CODE;

    private boolean postOnly = true;

    public MobileLoginFilter() {
        super(SecurityConstants.LOGIN_MOBILE_PROCESSING_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String mobile = obtainMobile(request);
        String smsCode = obtainSmsCode(request);

        if (mobile == null) {
            mobile = "";
        }
        if (smsCode == null) {
            smsCode = "";
        }
        mobile = mobile.trim();
        smsCode = smsCode.trim();
        MobileLoginToken token = new MobileLoginToken(mobile, smsCode);
        return this.getAuthenticationManager().authenticate(token);
    }

    @Nullable
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter(MOBILE_PARAMETER);
    }

    @Nullable
    protected String obtainSmsCode(HttpServletRequest request) {
        return request.getParameter(SMS_CODE_PARAMETER);
    }
}
