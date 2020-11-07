package com.kay.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kay.common.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author kay
 * @date 2019/9/9 21:33
 */
@Component
@Slf4j
public class LoginFailedHandler implements AuthenticationFailureHandler {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("onAuthenticationFailure 认证失败:{}", e.getMessage());
        ServerResponse<AuthenticationException> response = ServerResponse.createBySuccessMessage(e.getMessage(), e);
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(mapper.writeValueAsString(response));
        writer.flush();
        writer.close();
    }
}
