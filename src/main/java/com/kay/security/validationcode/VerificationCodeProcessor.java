package com.kay.security.validationcode;

public interface VerificationCodeProcessor {

    void send(String mobile);

    void validate(String mobile, String codeToCompare);

}
