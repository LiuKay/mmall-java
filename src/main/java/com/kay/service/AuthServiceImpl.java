package com.kay.service;

import com.kay.dao.UserMapper;
import com.kay.security.authentication.jwt.JwtTokenProvider;
import com.kay.security.validationcode.VerificationCodeProcessor;
import com.kay.vo.UserIdentityDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationCodeProcessor verificationCodeProcessor;

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
