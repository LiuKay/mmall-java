package com.kay.security.authentication;

import com.kay.security.authentication.AbstractSecurityConfiguration;
import com.kay.security.authentication.mobile.SmsCodeSecurityConfiguration;
import com.kay.security.properties.SecurityProperties;
import com.kay.security.validationcode.VerificationCodeSecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import static com.kay.security.properties.SecurityConstants.*;

@Configuration
public class BrowserSecurityConfiguration extends AbstractSecurityConfiguration {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private VerificationCodeSecurityConfiguration verificationCodeSecurityConfiguration;

    @Autowired
    private SmsCodeSecurityConfiguration smsCodeSecurityConfiguration;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        applyFormLoginConfig(http);

        http.apply(verificationCodeSecurityConfiguration)
                .and()
                .apply(smsCodeSecurityConfiguration)
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository)
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60)
                .and()
                .authorizeRequests()
                .antMatchers(
                        securityProperties.getBrowser().getLoginPage(),
                        AUTHENTICATION_URL,
                        LOGIN_MOBILE_PROCESSING_URL,
                        VERIFICATION_CODE_URL)
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .csrf().disable();
    }
}
