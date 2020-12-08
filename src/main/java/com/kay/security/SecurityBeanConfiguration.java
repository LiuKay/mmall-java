package com.kay.security;

import com.kay.dao.UserMapper;
import com.kay.security.properties.SecurityProperties;
import com.kay.security.validationcode.DefaultSmsCodeSender;
import com.kay.security.validationcode.SmsCodeGenerator;
import com.kay.security.validationcode.SmsCodeSender;
import com.kay.security.validationcode.VerificationCodeGenerator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author LiuKay
 * @since 2019/11/27
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityBeanConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MallPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserMapper userMapper) {
        return new MallUserDetailService(userMapper);
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsCodeGenerator")
    VerificationCodeGenerator smsCodeGenerator(SecurityProperties securityProperties) {
        return new SmsCodeGenerator(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }
}


