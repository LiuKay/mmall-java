package com.kay.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kay.common.ServerResponse;
import com.kay.security.properties.LoginResponseType;
import com.kay.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author LiuKay
 * @since 2019/11/28
 */
@Slf4j
public class BrowserAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;

    private final SecurityProperties securityProperties;

    public BrowserAuthenticationSuccessHandler(ObjectMapper objectMapper, SecurityProperties securityProperties) {
        this.objectMapper = objectMapper;
        this.securityProperties = securityProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {

        log.info(">>onAuthenticationSuccess:Login Success");

        if (LoginResponseType.JSON.equals(securityProperties.getBrowser().getLoginResponseType())) {
            response.setContentType("application/json;charset=UTF-8");
            ServerResponse<Authentication> serverResponse = ServerResponse.createBySuccess(authentication);
            response.getWriter().write(objectMapper.writeValueAsString(serverResponse));
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }


    }
}
