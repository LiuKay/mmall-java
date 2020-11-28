package com.kay.security.authentication.jwt;

import com.kay.security.JwtAuthenticationException;
import com.kay.vo.UserIdentityDTO;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationFailureHandler failureHandler;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider, AuthenticationFailureHandler failureHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.failureHandler = failureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(httpServletRequest);
        try {
            if (token != null) {
                UserIdentityDTO userIdentity = jwtTokenProvider.validateAndDecode(token);
                PreAuthenticatedAuthenticationToken auth =
                        new PreAuthenticatedAuthenticationToken(userIdentity.getUserName(), null,
                                AuthorityUtils.createAuthorityList(userIdentity.getRole().getAuthority()));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtAuthenticationException ex) {
            //this is very important, since it guarantees the user is not authenticated at all
            SecurityContextHolder.clearContext();
            this.failureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, ex);
            return;
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
