package com.kay.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author LiuKay
 * @since 2019/11/27
 */
@ConfigurationProperties(prefix = "application.security")
public class SecurityProperties {

    private VerificationCodeProperties validation = new VerificationCodeProperties();

    public VerificationCodeProperties getValidation() {
        return validation;
    }

    public void setValidation(VerificationCodeProperties validation) {
        this.validation = validation;
    }
}
