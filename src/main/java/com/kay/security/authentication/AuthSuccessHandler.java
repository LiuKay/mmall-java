package com.kay.security.authentication;

import com.kay.domain.Role;
import com.kay.security.authentication.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

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
        User user = (User) authentication.getDetails();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        GrantedAuthority role = Role.USER;
        if (authorities != null && !authorities.isEmpty()) {
            role = authorities.iterator().next();
        }
        String token = tokenProvider.createToken(user.getUsername(), role);
        log.info("Generate token:{}", token);
        response.getWriter().write(token);
    }
}
