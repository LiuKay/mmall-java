package com.kay.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kay.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author kay
 * @date 2019/9/9 21:29
 */
@Component
@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private ObjectMapper mapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功");
        HttpSession session = httpServletRequest.getSession(false);
        User user = (User) authentication.getPrincipal();
        String json = mapper.writeValueAsString(user);
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(json);
        writer.flush();
        writer.close();
    }
}
