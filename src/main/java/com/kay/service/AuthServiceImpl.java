package com.kay.service;

import com.kay.dao.UserMapper;
import com.kay.domain.User;
import com.kay.exception.NotFoundException;
import com.kay.security.authentication.jwt.JwtTokenProvider;
import com.kay.vo.UserIdentityDTO;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserIdentityDTO getUser(HttpServletRequest request) {
        return tokenProvider.getUserIdentity(request);
    }

    @Override
    public Integer getUserId(HttpServletRequest request) {
        return getUser(request).getUserId();
    }

    @Override
    public String getSmsCode(String mobile) {
        //TODO
        return null;
    }

    @Override
    public String login(String username, String password) {
        User user = Optional.ofNullable(userMapper.loadUserByUsername(username))
                .orElseThrow(() -> new NotFoundException("User not found."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password.");
        }

        return tokenProvider.createToken(username, user.getId(), user.getRole());
    }

    @Override
    public String loginByMobile(String mobile, String smsCode) {
        User user = Optional.ofNullable(userMapper.loadUserByPhone(mobile))
                .orElseThrow(() -> new NotFoundException("User not found."));
        //TODO check smsCode in redis or db
        return null;
    }
}
