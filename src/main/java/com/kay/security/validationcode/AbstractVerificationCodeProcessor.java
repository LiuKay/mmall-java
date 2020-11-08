package com.kay.security.validationcode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * Template Method for process verification code
 * 1. create a code
 * 2. store in session
 * 3. send to client or sms service
 *
 * @author LiuKay
 * @since 2019/12/7
 */
public abstract class AbstractVerificationCodeProcessor<T extends VerificationCode> implements VerificationCodeProcessor {

    private static final String SESSION_VALIDATION_CODE = "VALIDATION_CODE_";

    @Autowired
    private Map<String, VerificationCodeGenerator> validateCodeGenerators;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();


    @Override
    public void create(ServletWebRequest request) throws Exception {
        T verificationCode = createCode(request);
        store(request, verificationCode);
        send(request, verificationCode);
    }

    /**
     * Validate the verification code in the request against that in session
     *
     * @param request
     */
    @Override
    public void validate(ServletWebRequest request) {
        ValidationCodeType type = getValidationCodeType(request);
        String sessionKey = getSessionKey(request);

        T validationCode = (T) sessionStrategy.getAttribute(request, sessionKey);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), type.getParamName());
        } catch (ServletRequestBindingException e) {
            throw new VerificationCodeException("Verification code is required in the request .");
        }
        if (StringUtils.isBlank(codeInRequest)) {
            throw new VerificationCodeException("Verification code can not be null.");
        }

        if (validationCode.getCode() == null) {
            throw new VerificationCodeException("Verification code does not exist.");
        }

        if (validationCode.isExpired()) {
            throw new VerificationCodeException("Verification code is expired.");
        }

        if (!StringUtils.equalsAnyIgnoreCase(codeInRequest, validationCode.getCode())) {
            throw new VerificationCodeException("Verification code is incorrect.");
        }

        sessionStrategy.removeAttribute(request, sessionKey);
    }

    @Override
    public void send(ServletWebRequest request, Object verificationCode) throws Exception {
        send(request, (T) verificationCode);
    }

    /**
     * send verificationCode
     *
     * @param request
     * @param verificationCode
     */
    protected abstract void send(ServletWebRequest request, T verificationCode) throws Exception;

    /**
     * create a verificationCode by request
     *
     * @param request ServletWebRequest
     * @return specify verificationCode extends {@link VerificationCode}
     */
    private T createCode(ServletWebRequest request) {
        String type = getValidationCodeType(request).name().toLowerCase();
        // hard code the bean name
        String generatorBeanName = type + "CodeGenerator";
        VerificationCodeGenerator generator = validateCodeGenerators.get(generatorBeanName);
        if (generator == null) {
            throw new VerificationCodeException("Could not find VerificationCodeGenerator with bean name :" + generatorBeanName);
        }
        return (T) generator.generate(request);
    }

    /**
     * get ValidationCodeType from request URI
     *
     * @param request
     * @return
     */
    private ValidationCodeType getValidationCodeType(ServletWebRequest request) {
        // get type form current implementation class name of VerificationCodeProcessor
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "VerificationCodeProcessor");
        return ValidationCodeType.valueOf(type.toUpperCase());
    }

    /**
     * store verificationCode into session
     *
     * @param request
     * @param verificationCode
     */
    private void store(ServletWebRequest request, T verificationCode) {
        sessionStrategy.setAttribute(request, getSessionKey(request), verificationCode);
    }

    /**
     * get verificationCode from session
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request) {
        return SESSION_VALIDATION_CODE + getValidationCodeType(request).name();
    }

}
