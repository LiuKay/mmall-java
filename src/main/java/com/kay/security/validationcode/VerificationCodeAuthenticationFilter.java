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
@Component("verificationCodeFilter")
public class VerificationCodeAuthenticationFilter extends OncePerRequestFilter implements InitializingBean {

    private static final String FORM_LOGIN_URL = SecurityConstants.LOGIN_FORM_PROCESSING_URL;
    private static final String MOBILE_LOGIN_URL = SecurityConstants.LOGIN_MOBILE_PROCESSING_URL;

    @Autowired
    @Qualifier("authFailureHandler")
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private VerificationCodeProcessorHolder processorHolder;

    @Autowired
    private SecurityProperties securityProperties;

    private Map<String, ValidationCodeType> urlMap = new HashMap<>();

    // for url match
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();

        urlMap.put(FORM_LOGIN_URL, ValidationCodeType.IMAGE);
        urlMap.put(MOBILE_LOGIN_URL, ValidationCodeType.SMS);

        VerificationCodeProperties validation = securityProperties.getValidation();
        addUrl(validation.getImage().getUrl(), ValidationCodeType.IMAGE);
        addUrl(validation.getSms().getUrl(), ValidationCodeType.SMS);
    }

    /**
     * add the urls in properties file for each validation type
     *
     * @param urlConfig
     * @param type
     */
    protected void addUrl(String urlConfig, ValidationCodeType type) {
        if (StringUtils.isNoneBlank(urlConfig)) {
            String[] strings = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlConfig, ",");
            for (String url : strings) {
                urlMap.put(url, type);
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ValidationCodeType type = getValidationCodeType(request);
        //if has validation code in request,process the code
        if (null != type) {
            try {
                logger.info("Validate Code. Request \"" + request.getRequestURI() + "\" - Type: " + type);
                processorHolder.findProcessor(type).validate(new ServletWebRequest(request, response));
                logger.info("Validate Code SUCCESS.");
            } catch (VerificationCodeException e) {
                failureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private ValidationCodeType getValidationCodeType(HttpServletRequest request) {
        ValidationCodeType result = null;
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), "get")) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                }
            }
        }
        return result;
    }

}
