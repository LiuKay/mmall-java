package com.kay.security.validationcode.sms;

import com.kay.security.properties.SecurityConstants;
import com.kay.security.validationcode.AbstractVerificationCodeProcessor;
import com.kay.security.validationcode.VerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author LiuKay
 * @since 2019/12/7
 */
@Component("smsVerificationCodeProcessor")
public class SmsVerificationCodeProcessor extends AbstractVerificationCodeProcessor<VerificationCode> {

    private static final String DEFAULT_PARAMETER_NAME_MOBILE = SecurityConstants.REQUEST_PARAMETER_MOBILE;

    private final SmsCodeSender smsCodeSender;

    @Autowired
    public SmsVerificationCodeProcessor(SmsCodeSender smsCodeSender) {
        this.smsCodeSender = smsCodeSender;
    }

    @Override
    protected void send(ServletWebRequest request, VerificationCode verificationCode) throws
                                                                                      ServletRequestBindingException {
        String mobile = ServletRequestUtils
                .getRequiredStringParameter(request.getRequest(), DEFAULT_PARAMETER_NAME_MOBILE);
        smsCodeSender.send(mobile, verificationCode.getCode());
    }
}
