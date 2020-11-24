package com.kay.security.validationcode;

import com.kay.security.properties.SecurityProperties;
import com.kay.security.validationcode.image.ImageCodeGenerator;
import com.kay.security.validationcode.sms.DefaultSmsCodeSender;
import com.kay.security.validationcode.sms.SmsCodeGenerator;
import com.kay.security.validationcode.sms.SmsCodeSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LiuKay
 * @since 2019/12/6
 */
@Configuration
public class VerificationBeanConfiguration {

    private final SecurityProperties securityProperties;

    @Autowired
    public VerificationBeanConfiguration(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    VerificationCodeGenerator imageCodeGenerator() {
        return new ImageCodeGenerator(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsCodeGenerator")
    VerificationCodeGenerator smsCodeGenerator() {
        return new SmsCodeGenerator(securityProperties);
    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }
}
