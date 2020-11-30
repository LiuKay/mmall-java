package com.kay.security.authentication.mobile;

import com.kay.dao.UserMapper;
import com.kay.security.authentication.AuthFailureHandler;
import com.kay.security.authentication.AuthSuccessHandler;
import com.kay.security.validationcode.VerificationCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class MobileLoginSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private VerificationCodeProcessor verificationCodeProcessor;

    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        MobileLoginFilter mobileLoginFilter = new MobileLoginFilter();
        mobileLoginFilter.setAuthenticationSuccessHandler(authSuccessHandler);
        mobileLoginFilter.setAuthenticationFailureHandler(authFailureHandler);

        mobileLoginFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));

        MobileLoginAuthenticationProvider authenticationProvider = new MobileLoginAuthenticationProvider(userMapper, verificationCodeProcessor);

        builder.authenticationProvider(authenticationProvider)
                .addFilterBefore(mobileLoginFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
