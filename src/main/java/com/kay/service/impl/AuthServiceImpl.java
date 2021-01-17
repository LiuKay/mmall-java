package com.kay.service.impl;

import com.kay.security.authentication.jwt.JwtTokenProvider;
import com.kay.security.validationcode.VerificationCodeProcessor;
import com.kay.service.AuthService;
import com.kay.vo.UserIdentityDTO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider tokenProvider;

    private final VerificationCodeProcessor verificationCodeProcessor;

    @Autowired
    public AuthServiceImpl(JwtTokenProvider tokenProvider,
                           VerificationCodeProcessor verificationCodeProcessor) {
        this.tokenProvider = tokenProvider;
        this.verificationCodeProcessor = verificationCodeProcessor;
    }

    @Override
    public UserIdentityDTO getUser(HttpServletRequest request) {
        return tokenProvider.getUserIdentity(request);
    }

    @Override
    public Integer getUserId(HttpServletRequest request) {
        return getUser(request).getUserId();
    }

    @Override
    public void sendVerificationCode(String mobile) {
        verificationCodeProcessor.send(mobile);
    }

}
