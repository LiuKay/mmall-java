package com.kay.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author LiuKay
 * @since 2019/11/27
 */
@ConfigurationProperties(prefix = "application.security")
public class SecurityProperties {

    private BrowserSecurityProperties browser = new BrowserSecurityProperties();

    private VerificationCodeProperties validation = new VerificationCodeProperties();

    public BrowserSecurityProperties getBrowser() {
        return browser;
    }

    public void setBrowser(BrowserSecurityProperties browser) {
        this.browser = browser;
    }

    public VerificationCodeProperties getValidation() {
        return validation;
    }

    public void setValidation(VerificationCodeProperties validation) {
        this.validation = validation;
    }
}
