package com.kay.security.validationcode;

import com.kay.security.properties.SecurityConstants;
import com.kay.security.properties.SecurityProperties;
import com.kay.security.properties.VerificationCodeProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This filter is to validate the code verification code in the request and session
 *
 * @author LiuKay
 * @since 2019/12/5
 */
@Component
public class VerificationCodeAuthenticator {

    public boolean authenticate(String mobile, String smsCode) {
        return false;
    }

}
