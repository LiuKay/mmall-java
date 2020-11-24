package com.kay.security.authentication;

import com.kay.security.JwtAuthenticationException;
import com.kay.security.PhoneNumberNotFoundException;
import com.kay.security.validationcode.VerificationCodeException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component("authFailureHandler")
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error(">>onAuthenticationFailure.");
        if (exception instanceof JwtAuthenticationException ||
                exception instanceof PhoneNumberNotFoundException ||
                exception instanceof VerificationCodeException) {
            HttpStatus httpStatus;
            response.setContentType(MediaType.APPLICATION_JSON.toString());
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            if (exception instanceof JwtAuthenticationException) {
                httpStatus = HttpStatus.UNAUTHORIZED;
            } else if (exception instanceof PhoneNumberNotFoundException) {
                httpStatus = HttpStatus.NOT_FOUND;
            } else {
                httpStatus = HttpStatus.BAD_REQUEST;
            }
            response.setStatus(httpStatus.value());
            response.getWriter().write(exception.getMessage());
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
