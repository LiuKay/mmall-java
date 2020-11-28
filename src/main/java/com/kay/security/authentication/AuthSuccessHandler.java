package com.kay.security.authentication;

import com.kay.security.authentication.jwt.JwtTokenProvider;
import com.kay.vo.UserIdentityDTO;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("authSuccessHandler")
public class AuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    public AuthSuccessHandler(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        log.info(">>onAuthenticationSuccess:Login Success");
        Object details = authentication.getDetails();
        String token;
        if (details instanceof UserIdentityDTO) {
            UserIdentityDTO identityDTO = (UserIdentityDTO) details;
            token = tokenProvider.createToken(identityDTO.getUserName(),
                    identityDTO.getUserId(), identityDTO.getRole());
            log.info("Generate token:{}", token);
            response.getWriter().write(token);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "can not generate token.");
        }
    }
}
