package com.kay.service;

import com.kay.security.authentication.jwt.JwtTokenProvider;
import com.kay.vo.UserIdentityDTO;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthServiceImpl(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public UserIdentityDTO getUser(HttpServletRequest request) {
        return tokenProvider.getUserIdentity(request);
    }

    @Override
    public Integer getUserId(HttpServletRequest request) {
        return getUser(request).getUserId();
    }
}
