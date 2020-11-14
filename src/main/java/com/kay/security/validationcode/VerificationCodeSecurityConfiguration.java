package com.kay.security.validationcode;

import com.kay.security.authentication.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;

/**
 * @author LiuKay
 * @since 2019/12/8
 */
@Component("verificationCodeSecurityConfiguration")
public class VerificationCodeSecurityConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final Filter verificationCodeFilter;

    public VerificationCodeSecurityConfiguration(@Autowired @Qualifier("verificationCodeFilter") Filter verificationCodeFilter) {
        this.verificationCodeFilter = verificationCodeFilter;
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        builder.addFilterBefore(verificationCodeFilter, JwtTokenFilter.class);
    }
}
