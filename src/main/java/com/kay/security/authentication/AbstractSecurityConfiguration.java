package com.kay.security.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static com.kay.security.properties.SecurityConstants.AUTHENTICATION_URL;
import static com.kay.security.properties.SecurityConstants.LOGIN_FORM_PROCESSING_URL;

/**
 * @author LiuKay
 * @since 2019/12/8
 */
public abstract class AbstractSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    protected void applyFormLoginConfig(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(AUTHENTICATION_URL)
                .loginProcessingUrl(LOGIN_FORM_PROCESSING_URL)
                .successHandler(successHandler)
                .failureHandler(failureHandler);
    }
}
